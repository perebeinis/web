package com.tracker.dao.process.data;

import com.tracker.constants.BaseConstants;
import com.tracker.dao.process.data.impl.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by Perebeinis on 15.02.2018.
 */
public class DataProcessorFactory {

    final static Map<String, Supplier<DataProcessor>> map = new HashMap<>();
    static {
        map.put(BaseConstants.DEFAULT, DefaultDataProcessor::new);
        map.put(BaseConstants.ISSUE, TaskWithSingleExecutorDataProcessor::new);
        map.put(BaseConstants.MULTI_ISSUE, TaskWithMultipleExecutorDataProcessor::new);
        map.put(BaseConstants.MESSAGE, MessageDataProcessor::new);
        map.put(BaseConstants.NEWS, MessageDataProcessor::new);
        map.put(BaseConstants.COMMENTS, CommentsDataProcessor::new);
    }

    public String processData(String elementType, Object incomingDataObject, String elementId){
        Supplier<DataProcessor> element = map.get(elementType);
        if(element != null) {
            return element.get().processData(incomingDataObject, elementType, elementId);
        }else{
            return map.get(BaseConstants.DEFAULT).get().processData(incomingDataObject, elementType ,elementId);
        }
    }
}

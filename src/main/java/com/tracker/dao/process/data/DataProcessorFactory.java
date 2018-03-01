package com.tracker.dao.process.data;

import com.mongodb.client.MongoDatabase;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.process.data.impl.DefaultDataProcessor;
import com.tracker.dao.process.data.impl.MessageDataProcessor;
import com.tracker.dao.process.data.impl.SingleExecutorTaskDataProcessor;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by Perebeinis on 15.02.2018.
 */
public class DataProcessorFactory {

    final static Map<String, Supplier<DataProcessor>> map = new HashMap<>();
    static {
        map.put("default", DefaultDataProcessor::new);
        map.put("issue", SingleExecutorTaskDataProcessor::new);
        map.put("message", MessageDataProcessor::new);
    }

    public String processData(String elementType, JSONArray incomingDataObject, String elementId){
        Supplier<DataProcessor> element = map.get(elementType);
        if(element != null) {
            return element.get().processData(incomingDataObject, elementType, elementId);
        }else{
            return map.get(BaseConstants.DEFAULT).get().processData(incomingDataObject, elementType ,elementId);
        }
    }
}

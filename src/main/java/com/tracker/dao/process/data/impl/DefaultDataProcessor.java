package com.tracker.dao.process.data.impl;

import com.tracker.dao.process.data.DataProcessor;
import com.tracker.dao.process.data.DataProcessorService;
import org.json.JSONArray;

/**
 * Created by Perebeinis on 15.02.2018.
 */
public class DefaultDataProcessor implements DataProcessor {
    @Override
    public String processData(Object incomingDataObject, String elementType, String elementId) {
        return DataProcessorService.getInstance().createData(elementType,incomingDataObject);
    }

    @Override
    public String createData(Object incomingDataObject, String elementType, String elementId) {
        return "";
    }

    @Override
    public String updateData(Object incomingDataObject, String elementType, String elementId) {
        return "";
    }

    @Override
    public String removeData() {
        return "";
    }
}

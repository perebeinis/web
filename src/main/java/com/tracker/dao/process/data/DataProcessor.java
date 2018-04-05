package com.tracker.dao.process.data;

import org.json.JSONArray;

/**
 * Created by Perebeinis on 15.02.2018.
 */
public interface DataProcessor {
    String processData(Object incomingDataObject, String elementType, String elementId);
    String createData(Object incomingDataObject, String elementType, String elementId);
    String updateData(Object incomingDataObject, String elementType, String elementId);
    String removeData();
}

package com.tracker.dao.process.data;

import org.json.JSONArray;

/**
 * Created by Perebeinis on 15.02.2018.
 */
public interface DataProcessor {
    String processData(JSONArray incomingDataObject, String elementType, String elementId);
    String createData(JSONArray incomingDataObject, String elementType, String elementId);
    String updateData(JSONArray incomingDataObject, String elementType, String elementId);
    String removeData();
}

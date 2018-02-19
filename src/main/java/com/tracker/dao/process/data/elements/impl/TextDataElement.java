package com.tracker.dao.process.data.elements.impl;

import com.mongodb.client.MongoDatabase;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.process.data.elements.DataElement;
import org.json.JSONObject;

/**
 * Created by Perebeinis on 16.02.2018.
 */
public class TextDataElement implements DataElement{
    @Override
    public Object getData(MongoDatabase database, JSONObject incomingData) {
        return incomingData.get(BaseConstants.DATA);
    }
}

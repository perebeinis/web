package com.tracker.dao.process.audit.elements.impl;

import com.tracker.constants.BaseConstants;
import com.tracker.dao.process.audit.elements.AuditElement;
import org.bson.BSONObject;
import org.bson.Document;
import org.json.JSONObject;

/**
 * Created by Perebeinis on 16.02.2018.
 */
public class TextAuditElement implements AuditElement{
    @Override
    public Object createAuditData(String fieldName, Object incomingData) {
         Document jsonObject = new Document();
        jsonObject.put(BaseConstants.NAME, fieldName);
        jsonObject.put(BaseConstants.DATA, incomingData);
        return jsonObject;
    }
}

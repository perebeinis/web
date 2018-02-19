package com.tracker.dao.process.data.elements.impl;

import com.mongodb.client.MongoDatabase;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.process.data.elements.DataElement;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Perebeinis on 16.02.2018.
 */
public class UserAssocDataElement implements DataElement{
    @Override
    public Object getData(MongoDatabase database, JSONObject incomingData) {
        String fieldValue = (String) incomingData.get(BaseConstants.DATA);
        String[] idsArray = fieldValue.split(",");
        List<ObjectId> usersIds = new ArrayList<>();
        for (String userId : idsArray) {
            usersIds.add(new ObjectId(userId));
        }
        return usersIds;
    }
}

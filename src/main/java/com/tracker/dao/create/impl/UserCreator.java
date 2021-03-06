package com.tracker.dao.create.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.create.DataCreator;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Perebeinis on 10.01.2018.
 */
public class UserCreator implements DataCreator{

    @Override
    public void createData(MongoDatabase database, JSONArray incomingData) {
        MongoCollection<Document> collection = database.getCollection(BaseConstants.USERS_COLLECTION);
        Document document = new Document();
        for (Object formField : incomingData) {
            JSONObject formFieldElement = (JSONObject) formField;
            String fieldName = (String) formFieldElement.get("name");
            String fieldType = (String) formFieldElement.get("type");
            String fieldValue = (String) formFieldElement.get("data");

            if(fieldType.equals("file")){
                document.put(fieldName, DocumentCreator.createElement(database,formFieldElement));
            }else {
                document.put(fieldName, fieldValue);
            }

        }

        collection.insertOne(document);
        System.out.println("USER was created");
    }

    @Override
    public void updateData(MongoDatabase database) {

    }

    @Override
    public void removeData(MongoDatabase database) {

    }
}

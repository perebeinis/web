package com.tracker.dao.create.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.create.DataCreator;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Perebeinis on 23.01.2018.
 */
public class IssueCreator implements DataCreator{
    @Override
    public String createData(MongoDatabase database, JSONArray incomingData) {
        MongoCollection<Document> collection = database.getCollection(BaseConstants.ISSUE_COLLECTION);
        Document document = new Document();
        for (int i=0; i<100; i++){
            document = new Document();
            for (Object formField : incomingData) {
                JSONObject formFieldElement = (JSONObject) formField;
                String fieldName = (String) formFieldElement.get("name")+"_"+i;
                String fieldType = (String) formFieldElement.get("type");
                String fieldValue = (String) formFieldElement.get("data");

                if(fieldType.equals("file")) {
                    document.put(fieldName, DocumentCreator.createElement(database, formFieldElement));
                }else if(fieldType.equals("userAssoc")){
                    String [] idsArray = fieldValue.split(",");
                    List<ObjectId> ids = new ArrayList<>();

                    for (String userId : idsArray) {
                        ids.add( new ObjectId(userId));
                    }

                    document.put(fieldName,ids);
                }else {
                    document.put(fieldName, fieldValue);
                }

            }
            collection.insertOne(document);

        }


        ObjectId objectId = document.getObjectId(BaseConstants.DOCUMENT_ID);
        System.out.println("TASK was created");
        return objectId.toString();
    }

    @Override
    public void updateData(MongoDatabase database) {

    }

    @Override
    public void removeData(MongoDatabase database) {

    }
}

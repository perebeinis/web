package com.tracker.dao.create.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.create.DataCreator;
import org.bson.Document;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Perebeinis on 10.01.2018.
 */
public class DocumentCreator{

    public static Object createElement(MongoDatabase database, JSONObject incomingData) {
        MongoCollection<Document> collection = database.getCollection(BaseConstants.DOCUMENTS_COLLECTION);
        Document document = new Document();

        String fieldName = (String) incomingData.get("name");
        String fieldType = (String) incomingData.get("type");
        String fieldValue = (String) incomingData.get("data");
        String fileName = (String) incomingData.get("fileName");
        document.put("name",fieldName);
        document.put("data",fieldValue);
        document.put("fileName",fileName);

        collection.insertOne(document);
        System.out.println("Document was created");

        return document.get(BaseConstants.DOCUMENT_ID);
    }
}

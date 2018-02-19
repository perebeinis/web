package com.tracker.dao.process.data.elements.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.process.data.elements.DataElement;
import org.bson.Document;
import org.json.JSONObject;

/**
 * Created by Perebeinis on 15.02.2018.
 */
public class FileDataElement implements DataElement{
    @Override
    public Object getData(MongoDatabase database, JSONObject incomingData) {
        MongoCollection<Document> collection = database.getCollection(BaseConstants.DOCUMENTS_COLLECTION);
        Document document = new Document();

        String fieldName = (String) incomingData.get(BaseConstants.NAME);
        String fieldValue = (String) incomingData.get(BaseConstants.DATA);
        String fileName = (String) incomingData.get(BaseConstants.FILE_NAME);

        document.put(BaseConstants.NAME,fieldName);
        document.put(BaseConstants.DATA,fieldValue);
        document.put(BaseConstants.FILE_NAME,fileName);

        collection.insertOne(document);
        System.out.println("Document was created");
        return document.get(BaseConstants.DOCUMENT_ID);
    }
}

package com.tracker.dao.create.user;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tracker.dao.create.DataCreator;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class UserDataCreator implements DataCreator{

    private static final String USER_DETAILS_TABLE = "userdetails";

    @Override
    public void createData(MongoDatabase database, JSONObject incomingData) {
        System.out.println("aa");
        MongoCollection<Document> collection = database.getCollection(USER_DETAILS_TABLE);
        Document document = new Document();
        Iterator<?> keys = incomingData.keys();
        while(keys.hasNext() ) {
            String key = (String)keys.next();
            document.put(key,incomingData.get(key));
        }

        collection.insertOne(document);
        Object id = document.get("_id");
        System.out.println("data inserted");

    }

    @Override
    public void updateData(MongoDatabase database) {

    }

    @Override
    public void removeData(MongoDatabase database) {

    }

}
 
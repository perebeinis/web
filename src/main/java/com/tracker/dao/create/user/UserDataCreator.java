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
    public void createData(MongoDatabase database) {
        System.out.println("aa");
        MongoCollection<Document> collection = database.getCollection(USER_DETAILS_TABLE);
//        collection.insertOne(generateNewDocumentData());

        System.out.println("data inserted");

    }

    @Override
    public void updateData(MongoDatabase database) {

    }

    @Override
    public void removeData(MongoDatabase database) {

    }

    private Document generateNewDocumentData(){

        Document document = new Document();


        BasicDBObject properties = new BasicDBObject();
        document.put("user_id","test_1");
        document.put("password","test_1");
        document.put("firstName","yra");
        document.put("lastName","test");


        BasicDBList jsonArray = new BasicDBList();
        BasicDBObject helperAssociation = new BasicDBObject();
        helperAssociation.put("elementNode","11111111111");
        helperAssociation.put("elementType","user");
        helperAssociation.put("name","aassss");
        helperAssociation.put("title","bbbbbbbbbbb");
        jsonArray.add(helperAssociation);

        document.put("assocHelp", jsonArray);



        document.put("properties",properties);

          return  document;
    }




}

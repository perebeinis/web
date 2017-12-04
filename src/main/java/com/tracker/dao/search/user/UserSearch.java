package com.tracker.dao.search.user;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tracker.dao.search.AbstractDataSearch;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.mongodb.client.model.Aggregates.sort;

public class UserSearch extends AbstractDataSearch{
    private static final String userCollection = "userdetails";
    @Override
    public JSONArray getData(MongoDatabase mongoDatabase, JSONObject searchParams) {
        MongoCollection<Document> collection = mongoDatabase.getCollection(userCollection);
        Document query = new Document("firstName","sanok");

        FindIterable it = collection.find(query);

        ArrayList<Document> docs = new ArrayList();

        it.into(docs);
        JSONArray jsonArray = new JSONArray();

        for (Document doc : docs) {
            JsonObject gsonData = new JsonParser().parse(doc.toJson()).getAsJsonObject();
            String res = gsonData.toString();
            JSONObject jsonObject = new JSONObject(res);
            System.out.println(doc);
            jsonArray.put(jsonObject);
        }


        System.out.println("test");
        return jsonArray;
    }
}

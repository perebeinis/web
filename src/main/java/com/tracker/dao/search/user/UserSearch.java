package com.tracker.dao.search.user;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
import java.util.List;

import static com.mongodb.client.model.Aggregates.sort;

public class UserSearch extends AbstractDataSearch{
    private static final String userCollection = "userdetails";
    @Override
    public JSONArray getData(MongoDatabase mongoDatabase, JSONObject searchParams) {
        MongoCollection<Document> collection = mongoDatabase.getCollection(userCollection);
        Document query = new Document("firstName","sanok");

        FindIterable iterator = collection.find(query);
        ArrayList<Document> docs = new ArrayList();
        iterator.into(docs);

        JSONArray jsonArray = new JSONArray();
        docs.forEach((document) -> {
            jsonArray.put(new JSONObject(new JsonParser().parse(document.toJson()).getAsJsonObject().toString()));
        });

        System.out.println("finish search users");
        return jsonArray;
    }
}

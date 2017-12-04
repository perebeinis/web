package com.tracker.dao.search.user;

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
        Document query = new Document("user_id","test_1");

        FindIterable it = collection.find(query);

        ArrayList<Document> docs = new ArrayList();

        it.into(docs);
        JSONArray jsonArray = new JSONArray();

        for (Document doc : docs) {
            System.out.println(doc);
            jsonArray.put(doc.to);
        }


        System.out.println("test");
        return jsonArray;
    }
}

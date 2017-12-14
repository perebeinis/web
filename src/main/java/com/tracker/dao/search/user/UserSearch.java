package com.tracker.dao.search.user;

import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tracker.dao.search.AbstractDataSearch;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class UserSearch extends AbstractDataSearch{

    @Override
    public JSONObject getData(MongoDatabase mongoDatabase, JSONObject searchParams) {
        Integer start = (Integer) searchParams.get(startConst);
        Integer length = (Integer) searchParams.get(lengthConst);
        Integer draw = (Integer) searchParams.get(drawConst);
        ArrayList<Document> docs = new ArrayList();
        JSONObject searchData = (JSONObject) ((JSONObject) searchParams.get(search)).get(searchDataConst);
        BasicDBObject query = createSearchData(searchData);

        MongoCollection<Document> collection = mongoDatabase.getCollection(userCollection);

        FindIterable iteratorAll = collection.find(query);
        iteratorAll.into(docs);
        Integer count = docs.size();
        docs = new ArrayList();

        FindIterable iterator = collection.find(query).skip(start).limit(length);
        iterator.into(docs);

        JSONArray jsonArray = new JSONArray();
        docs.forEach((document) -> {
            jsonArray.put(new JSONObject(new JsonParser().parse(document.toJson()).getAsJsonObject().toString()));
        });


        JSONObject result = new JSONObject();
        result.put(drawConst, draw);
        result.put(recordsTotalConst, count);
        result.put(recordsFilteredConst, count);
        result.put(dataConst, jsonArray);

        System.out.println("finish search users from query "+query);

        return result;
    }

    private BasicDBObject createSearchData(JSONObject searchData){
        BasicDBObject regexQuery = new BasicDBObject();
        Iterator<?> keys = searchData.keys();
        while(keys.hasNext() ) {
            String key = (String)keys.next();
            regexQuery.put(key,new BasicDBObject("$regex", searchData.get(key)+".*")
                    .append("$options", "i"));
        }
        return regexQuery;
    }
}

package com.tracker.dao.search;

import com.google.gson.JsonArray;
import com.mongodb.client.MongoDatabase;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public abstract class AbstractDataSearch {

    public abstract JSONArray getData(MongoDatabase mongoDatabase, JSONObject searchParams);

}

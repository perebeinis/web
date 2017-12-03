package com.tracker.dao.search;

import com.mongodb.client.MongoDatabase;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class AbstractDataSearch {

    public abstract JSONArray getData(MongoDatabase mongoDatabase, JSONObject searchParams);

}

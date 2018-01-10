package com.tracker.dao.create;

import com.mongodb.client.MongoDatabase;
import org.json.JSONArray;
import org.json.JSONObject;

public interface DataCreator {

    void createData(MongoDatabase database, JSONArray incominData);

    void updateData(MongoDatabase database);

    void removeData(MongoDatabase database);
}

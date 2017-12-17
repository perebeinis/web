package com.tracker.dao.create;

import com.mongodb.client.MongoDatabase;
import org.json.JSONObject;

public interface DataCreator {

    void createData(MongoDatabase database, JSONObject incominData);

    void updateData(MongoDatabase database);

    void removeData(MongoDatabase database);
}

package com.tracker.dao.create;

import com.mongodb.client.MongoDatabase;

public interface DataCreator {

    void createData(MongoDatabase database);

    void updateData(MongoDatabase database);

    void removeData(MongoDatabase database);
}

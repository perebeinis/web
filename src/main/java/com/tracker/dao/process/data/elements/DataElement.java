package com.tracker.dao.process.data.elements;

import com.mongodb.client.MongoDatabase;
import org.json.JSONObject;

/**
 * Created by Perebeinis on 15.02.2018.
 */
public interface DataElement {

    Object getData(MongoDatabase database, JSONObject incomingData);
}

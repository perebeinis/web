package com.tracker.dao.search;

import com.mongodb.client.MongoDatabase;
import org.json.JSONObject;

/**
 * Created by Perebeinis on 19.12.2017.
 */
public interface DataSearcher {
    JSONObject searchData(MongoDatabase database, JSONObject searchDataObject);

    JSONObject getElementById(MongoDatabase database, String elementType,  String elementId);

    JSONObject updateElementById(MongoDatabase database, String elementType, String elementId, JSONObject dataForUpdate);
}

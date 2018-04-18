package com.tracker.dao.search.execute.impl;

import com.mongodb.client.MongoDatabase;
import com.tracker.dao.search.execute.DataSearchUtil;
import com.tracker.dao.search.execute.DataSearcher;
import org.json.JSONObject;

/**
 * Created by Perebeinis on 19.12.2017.
 */
public class DefaultSearcher extends DataSearchUtil implements DataSearcher {
    @Override
    public JSONObject searchData(MongoDatabase database, JSONObject searchDataObject) {
        return getData(database, searchDataObject);
    }

    @Override
    public JSONObject getElementById(MongoDatabase database, String elementType, String elementId) {
        return getDataById(database, elementType, elementId);
    }

    @Override
    public JSONObject updateElementById(MongoDatabase database, String elementType, String elementId, JSONObject dataForUpdate) {
        return updateDataById(database, elementType, elementId, dataForUpdate);
    }


}

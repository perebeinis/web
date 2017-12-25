package com.tracker.dao.search.impl;

import com.mongodb.client.MongoDatabase;
import com.tracker.dao.search.AbstractDataSearch;
import com.tracker.dao.search.DataSearcher;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Perebeinis on 19.12.2017.
 */
public class DefaultSearcher  extends AbstractDataSearch implements DataSearcher{
    @Override
    public JSONObject searchData(MongoDatabase database, JSONObject searchDataObject) {
        return getData(database,searchDataObject);
    }

    @Override
    public JSONObject getElementById(MongoDatabase database, String elementId) {
        return getDataById(database,elementId);
    }


}

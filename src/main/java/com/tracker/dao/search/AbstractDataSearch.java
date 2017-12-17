package com.tracker.dao.search;

import com.mongodb.client.MongoDatabase;
import org.json.JSONObject;

public abstract class AbstractDataSearch {
    public static final String userCollection = "userdetails";
    public static final String startConst = "start";
    public static final String lengthConst = "length";
    public static final String drawConst = "draw";
    public static final String orderConst = "order";
    public static final String recordsTotalConst = "recordsTotal";
    public static final String recordsFilteredConst = "recordsFiltered";
    public static final String dataConst = "data";
    public static final String search = "search";
    public static final String searchDataConst = "searchData";

    public abstract JSONObject getData(MongoDatabase mongoDatabase, JSONObject searchParams);

}

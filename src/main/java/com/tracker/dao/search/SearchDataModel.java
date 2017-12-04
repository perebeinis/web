package com.tracker.dao.search;

import org.json.JSONArray;
import org.json.JSONObject;

public class SearchDataModel {
    private String searchType;
    private JSONObject searchData;


    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public JSONObject getSearchData() {
        return searchData;
    }

    public void setSearchData(JSONObject searchData) {
        this.searchData = searchData;
    }
}

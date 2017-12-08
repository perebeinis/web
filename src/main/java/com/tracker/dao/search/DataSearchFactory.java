package com.tracker.dao.search;

import com.google.gson.JsonArray;
import com.mongodb.client.MongoDatabase;
import com.tracker.dao.search.user.UserSearch;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class DataSearchFactory {

    @Autowired
    private MongoDatabase database;

    private static final String searhTypeConstant = "searchType";
    private static final String searhDataConstant = "searchData";
    private static final String userTypeConstant = "user";

    public JSONArray getData(JSONObject jsonObject){
        String searchType = (String) jsonObject.get(searhTypeConstant);
        JSONArray jsonArray = new JSONArray();

        switch (searchType){
            case userTypeConstant:
                UserSearch userSearch = new UserSearch();
                jsonArray =  userSearch.getData(database, jsonObject.getJSONObject(searhDataConstant));
                break;
            default:
                System.out.println("DEFAULT SEARCH");
        }

        return jsonArray;
    }

}

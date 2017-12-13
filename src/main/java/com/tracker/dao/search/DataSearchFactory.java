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

    public JSONObject getData(JSONObject jsonObject){
        String searchType = (String) ((JSONObject) jsonObject.get("search")).get(searhTypeConstant);
        JSONObject result = new JSONObject();

        switch (searchType){
            case userTypeConstant:
                UserSearch userSearch = new UserSearch();
                result =  userSearch.getData(database, jsonObject);
                break;
            default:
                System.out.println("DEFAULT SEARCH");
        }

        return result;
    }

}

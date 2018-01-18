package com.tracker.dao.search;

import com.google.gson.JsonArray;
import com.mongodb.client.MongoDatabase;
import com.tracker.cards.CardData;
import com.tracker.cards.impl.IssueCardData;
import com.tracker.cards.impl.UserCardData;
import com.tracker.dao.search.impl.DefaultSearcher;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class DataSearchFactory {

    @Autowired
    private MongoDatabase database;

    private static final String searhTypeConstant = "searchType";
    private static final String searhDataConstant = "searchData";
    private static final String userTypeConstant = "user";

    final static Map<String, Supplier<DataSearcher>> map = new HashMap<>();
    static {
        map.put("element", DefaultSearcher::new);
        map.put("user", DefaultSearcher::new);
    }

    public JSONObject searchData(String elementType, JSONObject searchDataObject){
        Supplier<DataSearcher> element = map.get(elementType);
        if(element != null) {
            return element.get().searchData(database,searchDataObject);
        }
        throw new IllegalArgumentException("No such shape " + elementType);
    }

    public JSONObject searchDataById(String elementType, String elementId){
        Supplier<DataSearcher> element = map.get(elementType);
        if(element != null) {
            return element.get().getElementById(database, elementType, elementId);
        }
        throw new IllegalArgumentException("No such shape " + elementType);
    }

}

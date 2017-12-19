package com.tracker.dao.search;

import com.mongodb.client.MongoDatabase;
import com.tracker.dao.search.impl.DefaultSearcher;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GetElementFactory {

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

    public JSONObject searchData(String elementType, String elementId){
        Supplier<DataSearcher> element = map.get(elementType);
        if(element != null) {
            return element.get().getElementById(database, elementId);
        }
        throw new IllegalArgumentException("No such shape " + elementType);
    }

}

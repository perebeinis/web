package com.tracker.dao.create;

import com.mongodb.client.MongoDatabase;
import com.tracker.dao.create.impl.IssueCreator;
import com.tracker.dao.create.impl.UserCreator;
import com.tracker.dao.search.DataSearcher;
import com.tracker.dao.search.impl.DefaultSearcher;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by Perebeinis on 10.01.2018.
 */
public class DataCreatorFactory {

    @Autowired
    private MongoDatabase database;

    final static Map<String, Supplier<DataCreator>> dataCreatorMap = new HashMap<>();
    static {
        dataCreatorMap.put("user", UserCreator::new);
        dataCreatorMap.put("issue", IssueCreator::new);
    }

    public String createData(String elementType, JSONArray incomingDataObject){
        Supplier<DataCreator> element = dataCreatorMap.get(elementType);
        if(element != null) {
            return element.get().createData(database, incomingDataObject);
        }
        throw new IllegalArgumentException("No such data creator " + elementType);
    }

}

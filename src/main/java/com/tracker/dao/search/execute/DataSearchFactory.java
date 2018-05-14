package com.tracker.dao.search.execute;

import com.mongodb.client.MongoDatabase;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.search.execute.impl.DefaultSearcher;
import com.tracker.dao.search.execute.impl.MultiIssueSearcher;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DataSearchFactory {

    @Autowired
    private MongoDatabase database;

    final static Map<String, Supplier<DataSearcher>> map = new HashMap<>();

    static {
        map.put(BaseConstants.DEFAULT, DefaultSearcher::new);
        map.put(BaseConstants.MULTI_ISSUE, MultiIssueSearcher::new);
    }

    public JSONObject searchData(String elementType, JSONObject searchDataObject) {
        Supplier<DataSearcher> element = map.get(elementType);
        if (element != null) {
            return element.get().searchData(database, searchDataObject);
        } else {
            return map.get(BaseConstants.DEFAULT).get().searchData(database, searchDataObject);
        }
    }

    public JSONObject searchDataById(String elementType, String elementId) {
        Supplier<DataSearcher> element = map.get(elementType);
        if (element != null) {
            return element.get().getElementById(database, elementType, elementId);
        } else {
            return map.get(BaseConstants.DEFAULT).get().getElementById(database, elementType, elementId);
        }
    }

    public JSONObject updateElementById(String elementType, String elementId, JSONObject dataForUpdate) {
        Supplier<DataSearcher> element = map.get(elementType);
        if (element != null) {
            return element.get().updateElementById(database, elementType, elementId, dataForUpdate);
        } else {
            return map.get(BaseConstants.DEFAULT).get().updateElementById(database, elementType, elementId, dataForUpdate);
        }
    }
}

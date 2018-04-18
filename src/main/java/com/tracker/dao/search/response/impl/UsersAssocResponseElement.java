package com.tracker.dao.search.response.impl;

import com.tracker.constants.BaseConstants;
import com.tracker.dao.process.data.elements.impl.UserAssocDataElement;
import com.tracker.dao.search.response.SearchResponseElement;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Perebeinis on 16.03.2018.
 */
public class UsersAssocResponseElement implements SearchResponseElement{
    @Override
    public Object getData(Object incomingData) {
        JSONArray incomingDataObjArray = (JSONArray) incomingData;
        StringBuilder result = new StringBuilder();
        for (Object assocObject : incomingDataObjArray) {
            JSONObject data = (JSONObject) assocObject;
            result.append(data.get(BaseConstants.LAST_NAME)).append(" ").append(data.get(BaseConstants.LAST_NAME)).append(BaseConstants.HTMl_LINE_BREAK);
        }
        return result;
    }
}

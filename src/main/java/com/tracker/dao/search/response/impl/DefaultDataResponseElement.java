package com.tracker.dao.search.response.impl;

import com.tracker.dao.search.response.SearchResponseElement;
import org.json.JSONObject;

/**
 * Created by Perebeinis on 16.03.2018.
 */
public class DefaultDataResponseElement implements SearchResponseElement {
    @Override
    public Object getData(Object incomingData) {
        return incomingData;
    }
}

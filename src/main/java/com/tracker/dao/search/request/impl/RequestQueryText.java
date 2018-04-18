package com.tracker.dao.search.request.impl;

import com.mongodb.BasicDBObject;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.search.request.CreateRequestQuery;
import org.bson.conversions.Bson;
import org.springframework.util.StringUtils;

/**
 * Created by Perebeinis on 11.01.2018.
 */
public class RequestQueryText implements CreateRequestQuery{
    @Override
    public Bson createQueryForElement(String searchName, String searchValue) {
        if(StringUtils.isEmpty(searchValue)) return null;
        BasicDBObject regexQuery = new BasicDBObject();
        regexQuery.put(searchName, new BasicDBObject(BaseConstants.REGEX, searchValue+".*").append(BaseConstants.OPTIONS, "i"));
        return regexQuery;
    }
}

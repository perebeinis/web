package com.tracker.dao.search.request.impl;

import com.mongodb.BasicDBObject;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.search.request.CreateRequestQuery;
import org.bson.conversions.Bson;

/**
 * Created by Perebeinis on 11.01.2018.
 */
public class CreateRequestQueryText implements CreateRequestQuery{
    @Override
    public Bson createQueryForElement(String searchName, String searchValue) {
        BasicDBObject regexQuery = new BasicDBObject();
        regexQuery.put(searchName, new BasicDBObject(BaseConstants.REGEX, searchValue+".*").append(BaseConstants.OPTIONS, "i"));
        return regexQuery;
    }
}

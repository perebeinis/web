package com.tracker.dao.search.request.impl;

import com.mongodb.BasicDBObject;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.search.request.CreateRequestQuery;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.util.StringUtils;

/**
 * Created by Perebeinis on 11.01.2018.
 */
public class RequestQueryAddressee implements CreateRequestQuery {
    @Override
    public Bson createQueryForElement(String searchName, String searchValue) {
        if(StringUtils.isEmpty(searchValue)) return null;
        BasicDBObject regexQuery = new BasicDBObject();
        regexQuery.put(searchName, new ObjectId(searchValue));
        return regexQuery;
    }
}

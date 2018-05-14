package com.tracker.dao.search.request.impl;

import com.mongodb.BasicDBObject;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.search.request.CreateRequestQuery;
import org.bson.conversions.Bson;
import org.springframework.util.StringUtils;

/**
 * Created by Perebeinis on 16.03.2018.
 */
public class RequestQuerySearchTextOr implements CreateRequestQuery{
    @Override
    public Bson createQueryForElement(String searchName, String searchValue) {
        if(StringUtils.isEmpty(searchValue)) return null;
        return new BasicDBObject(
                BaseConstants.OR ,
                new BasicDBObject(searchName, new BasicDBObject(BaseConstants.REGEX, searchValue+".*").append(BaseConstants.OPTIONS, "i")));

    }
}

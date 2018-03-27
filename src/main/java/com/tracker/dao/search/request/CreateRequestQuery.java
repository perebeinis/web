package com.tracker.dao.search.request;

import com.mongodb.BasicDBObject;
import org.bson.conversions.Bson;

/**
 * Created by Perebeinis on 11.01.2018.
 */
public interface CreateRequestQuery {
    Bson createQueryForElement(String searchName, String searchValue);
}

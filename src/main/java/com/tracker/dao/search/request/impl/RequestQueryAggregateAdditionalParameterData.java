package com.tracker.dao.search.request.impl;

import com.tracker.constants.BaseConstants;
import com.tracker.dao.search.request.CreateRequestQuery;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * Created by Perebeinis on 16.03.2018.
 */
public class RequestQueryAggregateAdditionalParameterData {
    public Bson createQueryForElement(String searchName, Object searchValue) {
        return new Document(searchName, searchValue);
    }
}

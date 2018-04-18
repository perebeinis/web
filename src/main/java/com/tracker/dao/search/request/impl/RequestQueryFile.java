package com.tracker.dao.search.request.impl;

import com.mongodb.BasicDBObject;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.search.request.CreateRequestQuery;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * Created by Perebeinis on 11.01.2018.
 */
public class RequestQueryFile implements CreateRequestQuery {
    @Override
    public Bson createQueryForElement(String searchName, String searchValue) {
        return new Document(BaseConstants.LOOKUP,
                new Document(BaseConstants.FROM, BaseConstants.DOCUMENTS_COLLECTION)
                        .append(BaseConstants.LOCAL_FIELD, searchName)
                        .append(BaseConstants.FOREIGN_FIELD, BaseConstants.DOCUMENT_ID)
                        .append(BaseConstants.AS, searchName));
    }
}

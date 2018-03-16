package com.tracker.dao.search.request.impl;

import com.mongodb.BasicDBObject;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.search.request.CreateRequestQuery;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * Created by Perebeinis on 13.03.2018.
 */
public class AuditDataQuery implements CreateRequestQuery{
    @Override
    public Bson createQueryForElement(String searchName, String searchValue) {
        Document dataAssoc = new Document(BaseConstants.DATA, new BasicDBObject(BaseConstants.REGEX, searchValue+".*").append(BaseConstants.OPTIONS, "i"));
        Bson request = new Document(BaseConstants.AUDIT_DATA, new Document(BaseConstants.ELEM_MATCH, dataAssoc));
        return request;
    }
}

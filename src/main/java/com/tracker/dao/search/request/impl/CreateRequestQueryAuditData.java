package com.tracker.dao.search.request.impl;

import com.mongodb.BasicDBObject;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.search.request.CreateRequestQuery;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.util.StringUtils;

/**
 * Created by Perebeinis on 11.01.2018.
 */
public class CreateRequestQueryAuditData implements CreateRequestQuery{
    @Override
    public Bson createQueryForElement(String searchName, String searchValue) {
        if(StringUtils.isEmpty(searchValue)) return null;
        Document regexQuery = new Document(BaseConstants.DATA, new BasicDBObject(BaseConstants.REGEX, searchValue+".*").append(BaseConstants.OPTIONS, "i"));
        Bson auditData = new Document(BaseConstants.MATCH, new Document(BaseConstants.AUDIT_DATA, new Document(BaseConstants.ELEM_MATCH, regexQuery)));

        return auditData;
    }
}

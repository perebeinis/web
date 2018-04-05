package com.tracker.dao.search.request.impl;

import com.tracker.constants.BaseConstants;
import com.tracker.dao.search.request.CreateRequestQuery;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * Created by Perebeinis on 04.04.2018.
 */
public class CommentsAssocQuery implements CreateRequestQuery {
    @Override
    public Bson createQueryForElement(String searchName, String searchValue) {
        Bson lookup = new Document(BaseConstants.LOOKUP,
                new Document(BaseConstants.FROM, BaseConstants.COMMENTS)
                        .append(BaseConstants.LOCAL_FIELD, searchName)
                        .append(BaseConstants.FOREIGN_FIELD, BaseConstants.DOCUMENT_ID)
                        .append(BaseConstants.AS, searchName));
        return lookup;
    }
}

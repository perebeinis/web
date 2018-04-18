package com.tracker.dao.search.request.impl;

import com.mongodb.BasicDBObject;
import com.tracker.config.security.authentification.CustomUserObject;
import com.tracker.config.security.authentification.impl.UserDetailsServiceImpl;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.search.request.CreateRequestQuery;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Perebeinis on 11.01.2018.
 */
public class RequestQueryUserAssoc implements CreateRequestQuery {
    @Override
    public Bson createQueryForElement(String searchName, String searchValue) {
        Bson request = new BsonDocument();
        if (searchValue.equals(BaseConstants.MY_ID)) {
            WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
            UserDetailsServiceImpl userDetailsService = (UserDetailsServiceImpl) context.getBean(BaseConstants.CUSTOM_USER_DETAILS_SERVICE);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserObject customUserObject = userDetailsService.loadUserDataByUsername(authentication.getName());

            List<ObjectId> ids = new ArrayList<>();
            ids.add(customUserObject.getUserId());
            request = new BasicDBObject(BaseConstants.MATCH, new BasicDBObject(searchName, new BasicDBObject(BaseConstants.IN, ids)));
        } else {
            request = new Document(BaseConstants.LOOKUP,
                    new Document(BaseConstants.FROM, BaseConstants.USERS_COLLECTION)
                            .append(BaseConstants.LOCAL_FIELD, searchName)
                            .append(BaseConstants.FOREIGN_FIELD, BaseConstants.DOCUMENT_ID)
                            .append(BaseConstants.AS, searchName));
        }
        return request;
    }
}

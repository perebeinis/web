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

/**
 * Created by Perebeinis on 13.03.2018.
 */
public class RequestQueryCurrentExecutor implements CreateRequestQuery {
    @Override
    public Bson createQueryForElement(String searchName, String searchValue) {
//        Bson requestData = new Document(BaseConstants.MATCH, new Document(searchName, new ObjectId(searchValue)));
        Bson requestData = new BsonDocument();

        if(searchValue.equals(BaseConstants.MY_ID)){
            WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
            UserDetailsServiceImpl userDetailsService = (UserDetailsServiceImpl) context.getBean(BaseConstants.CUSTOM_USER_DETAILS_SERVICE);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserObject customUserObject = userDetailsService.loadUserDataByUsername(authentication.getName());
            requestData = new Document(BaseConstants.MATCH, new Document(searchName, customUserObject.getUserId()));
        }

        return requestData;
    }
}

package com.tracker.dao.process.audit.elements.impl;

import com.tracker.config.SpringWebConfig;
import com.tracker.config.security.authentification.CustomUserObject;
import com.tracker.config.security.authentification.impl.UserDetailsServiceImpl;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.process.audit.elements.AuditElement;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import sun.awt.AppContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Perebeinis on 16.02.2018.
 */
public class UserAuditElement implements AuditElement {
    @Override
    public Object createAuditData(String fieldName, Object incomingData) {
        List<ObjectId> usersIds = (List<ObjectId>) incomingData;
        List<String> data = new ArrayList<>();
        WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        UserDetailsServiceImpl userDetailsService = (UserDetailsServiceImpl) context.getBean(BaseConstants.CUSTOM_USER_DETAILS_SERVICE);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        for (ObjectId usersId : usersIds) {
            CustomUserObject customUserObject = userDetailsService.loadUserById(usersId);
            JSONObject userData = customUserObject.getAllUserData();
            String userFullName = userData.get(BaseConstants.FIRST_NAME) + " "+userData.get(BaseConstants.LAST_NAME);
            data.add(userFullName);
            System.out.println("aaa");
        }
        Document jsonObject = new Document();
        jsonObject.put(BaseConstants.NAME, fieldName);
        jsonObject.put(BaseConstants.DATA, data);
        return jsonObject;
    }
}

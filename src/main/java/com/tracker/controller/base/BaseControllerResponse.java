package com.tracker.controller.base;

import com.tracker.config.localization.MessageResolveService;
import com.tracker.config.security.authentification.CustomUserObject;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.search.execute.DataSearchFactory;
import com.tracker.view.elements.ViewElementsDataFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.ModelMap;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BaseControllerResponse {

    private final String defaultSearchNewsRequestForMenu = "{\"draw\":10,\"columns\":[{\"data\":\"elementName\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"description\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"creatorAssoc\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}},{\"data\":\"created\",\"name\":\"\",\"searchable\":true,\"orderable\":true,\"search\":{\"value\":\"\",\"regex\":false}}],\"order\":[{\"column\":3,\"dir\":\"desc\"}],\"start\":0,\"length\":50,\"search\":{\"searchType\":\"news\",\"searchData\":{}}}";

    @Autowired
    private UserDetailsService customUserDetailsService;

    @Autowired
    private MessageResolveService messageResolveService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ViewElementsDataFactory viewElementsDataFactory;

    @Autowired
    private DataSearchFactory dataSearchFactory;

    public ModelMap getBaseResponseData(ModelMap model, Authentication authentication, Locale locale){
        model.addAttribute(BaseConstants.HEADER_LIST, viewElementsDataFactory.getDefaultViewElementData().getWindowItemsForUser(BaseConstants.HEADER_ELEMENT_CONSTANT));
        model.addAttribute(BaseConstants.MENU_LIST, viewElementsDataFactory.getDefaultViewElementData().getWindowItemsForUser(BaseConstants.MENU_ELEMENT_CONSTANT));
        model.addAttribute(BaseConstants.NEWS_LIST, getNews());
        model.addAttribute(BaseConstants.MESSAGES, messageResolveService.getMessages(messageSource, locale));
        if(authentication != null){
            CustomUserObject userDetails = customUserDetailsService.loadUserDataByUsername(authentication.getName());
            JSONObject userData = userDetails.getAllUserData();
            model.addAttribute(BaseConstants.USER_DATA, userData);
        }else{
            System.out.println("FIRST APP START");
        }
        return model;
    }

    public Map<String, Object> getBaseResponseDataJson(ModelMap model, Authentication authentication, Locale locale){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(BaseConstants.HEADER_LIST, viewElementsDataFactory.getDefaultViewElementData().getWindowItemsForUser(BaseConstants.HEADER_ELEMENT_CONSTANT));
        resultMap.put(BaseConstants.MENU_LIST, viewElementsDataFactory.getDefaultViewElementData().getWindowItemsForUser(BaseConstants.MENU_ELEMENT_CONSTANT));
        resultMap.put(BaseConstants.NEWS_LIST, getNews());
        resultMap.put(BaseConstants.MESSAGES, messageResolveService.getMessages(messageSource, locale));
        CustomUserObject userDetails = customUserDetailsService.loadUserDataByUsername(authentication.getName());
        JSONObject userData = userDetails.getAllUserData();
        resultMap.put(BaseConstants.USER_DATA, userData);
        return resultMap;
    }

    public JSONObject getNews(){
        String searchData = defaultSearchNewsRequestForMenu;
        JSONObject jsonObject = new JSONObject(searchData);
        return dataSearchFactory.searchData(BaseConstants.DEFAULT, jsonObject);
    }
}

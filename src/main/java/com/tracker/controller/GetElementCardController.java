package com.tracker.controller;

import com.mongodb.client.MongoDatabase;
import com.tracker.cards.CardDataFactory;
import com.tracker.cards.user.UserCard;
import com.tracker.dao.search.DataSearchFactory;
import com.tracker.dynamic.FrontElementConfigurationParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;

@Controller
public class GetElementCardController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private FrontElementConfigurationParser frontElementConfigurationParser;

    @Autowired
    private MongoDatabase database;

    @Autowired
    private DataSearchFactory dataSearchFactory;

    @Autowired
    private CardDataFactory cardDataFactory;

    private static final String userType = "user";


    @RequestMapping(value = "/create-element", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getElementCard(Locale locale, ModelMap model, Authentication authentication, @RequestParam("type") String type) {
        model = cardDataFactory.getCardData(type,model,null);
        return "create-user-card";
    }

    @RequestMapping(value = "/get-element", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getElementCardExist(Locale locale, ModelMap model, Authentication authentication, @RequestParam("type") String type, @RequestParam("id") String id) {
        model = cardDataFactory.getCardData(type,model,id);
        return "create-user-card";
    }


}

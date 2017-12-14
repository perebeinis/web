package com.tracker.controller;

import com.mongodb.client.MongoDatabase;
import com.tracker.cards.user.UserCard;
import com.tracker.dao.UserData;
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

/**
 * Created by Perebeinis on 13.12.2017.
 */
@Controller
public class SearchInboxesController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private FrontElementConfigurationParser frontElementConfigurationParser;

    @Autowired
    private MongoDatabase database;

    @Autowired
    private UserCard userCard;

    @Autowired
    private DataSearchFactory dataSearchFactory;


    @RequestMapping(value = "/search", method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String search(Locale locale, ModelMap model, Authentication authentication, @RequestParam("filter") String filter) {
        String welcome = messageSource.getMessage("bug-tracker.title", new Object[]{""}, locale);
        model.addAttribute("title", welcome);
        String loginMsg = messageSource.getMessage("loginMsg.title", new Object[]{""}, locale);
        model.addAttribute("loginMsg", loginMsg);
        model.addAttribute("headerList", frontElementConfigurationParser.parseHeaderMenuButtons(authentication));
        model.addAttribute("searchers", frontElementConfigurationParser.getFilterSearchers(filter, locale));
        model.addAttribute("menuList", frontElementConfigurationParser.parseMenuButtons(authentication));

        UserData userData = new UserData();
        userData.getUserData(database);

        JSONObject jsonArray = userCard.getUserData();
        model.addAttribute("searchData", jsonArray);

        return "search-data";
    }

}

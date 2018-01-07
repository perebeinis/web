package com.tracker.controller;

import com.mongodb.client.MongoDatabase;
import com.tracker.config.localization.MessageResolveService;
import com.tracker.config.security.authentification.CustomUserObject;
import com.tracker.controller.base.BaseControllerResponce;
import com.tracker.dao.search.DataSearchFactory;
import com.tracker.dynamic.FrontElementConfigurationParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private BaseControllerResponce baseControllerResponce;

    @Autowired
    private FrontElementConfigurationParser frontElementConfigurationParser;


    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = {"application/xml; charset=UTF-8"})
    public String search(Locale locale, ModelMap model, Authentication authentication, @RequestParam("filter") String filter) {
        model = baseControllerResponce.getBaseResponceData(model,authentication, locale);
        model.addAttribute("searchers", frontElementConfigurationParser.getFilterSearchers(filter, locale));
        return "search-data";
    }



}

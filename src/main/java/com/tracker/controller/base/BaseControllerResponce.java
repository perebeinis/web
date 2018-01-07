package com.tracker.controller.base;

import com.tracker.config.localization.MessageResolveService;
import com.tracker.config.security.authentification.CustomUserObject;
import com.tracker.dynamic.FrontElementConfigurationParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.ModelMap;

import java.util.Locale;

public class BaseControllerResponce {

    @Autowired
    private UserDetailsService customUserDetailsService;

    @Autowired
    private MessageResolveService messageResolveService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private FrontElementConfigurationParser frontElementConfigurationParser;

    public ModelMap getBaseResponceData(ModelMap model, Authentication authentication, Locale locale){
        model.addAttribute("headerList", frontElementConfigurationParser.parseHeaderMenuButtons());
        model.addAttribute("menuList", frontElementConfigurationParser.parseMenuButtons());
        model.addAttribute("messages", messageResolveService.getMessages(messageSource, locale));
        CustomUserObject userDetails = customUserDetailsService.loadUserDataByUsername(authentication.getName());
        JSONObject userData = userDetails.getAllUserData();
        model.addAttribute("userData", userData);

        return model;
    }
}

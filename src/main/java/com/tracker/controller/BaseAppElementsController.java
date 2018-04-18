package com.tracker.controller;

import com.tracker.constants.BaseConstants;
import com.tracker.dao.process.data.DataProcessorFactory;
import com.tracker.mail.send.impl.GmailSender;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Locale;
import java.util.UUID;

@Controller
public class BaseAppElementsController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserDetailsService customUserDetailsService;

    @Autowired
    private DataProcessorFactory dataProcessorFactory;

    @RequestMapping("/")
    public String root(Locale locale) {
        return "redirect:/search?filter=news";
    }

    @RequestMapping("/login")
    public String login(Locale locale, ModelMap model) {
        return "login";
    }

    @RequestMapping(value = "/registration-new-user",  method = RequestMethod.GET)
    public String registration(Locale locale, ModelMap model) {
        model.addAttribute(BaseConstants.REGISTRATION_TITLE, messageSource.getMessage(BaseConstants.REGISTRATION_TITLE, new Object[]{""}, locale));
        model.addAttribute(BaseConstants.FIRST_NAME, messageSource.getMessage(BaseConstants.TITLE_FIRST_NAME, new Object[]{""}, locale));
        model.addAttribute(BaseConstants.LAST_NAME, messageSource.getMessage(BaseConstants.TITLE_LAST_NAME, new Object[]{""}, locale));
        model.addAttribute(BaseConstants.USER_EMAIL, messageSource.getMessage(BaseConstants.TITLE_EMAIL, new Object[]{""}, locale));
        return "registration-new-user";
    }

    @RequestMapping(value = "/registration-new-user",  method = RequestMethod.POST)
    public String registrationPost(Locale locale, ModelMap model, @RequestBody MultiValueMap<String,String> formData) {
        String userName = formData.get(BaseConstants.USERNAME).get(0);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
        if(userDetails != null){
            model.addAttribute(BaseConstants.USER_ALREADY_EXIST_IN_SYSTEM, messageSource.getMessage(BaseConstants.USER_ALREADY_EXIST_IN_SYSTEM, new Object[]{""}, locale));
        } else {
            JSONArray userData = new JSONArray();
            userData.put(createElementData(BaseConstants.USER_ID, userName));
            UUID uuid = UUID.randomUUID();
            String pass = "test123";
            userData.put(createElementData(BaseConstants.USER_PASS, pass));
            userData.put(createElementData(BaseConstants.USER_EMAIL, formData.get(BaseConstants.USER_EMAIL).get(0)));
            userData.put(createElementData(BaseConstants.USER_ROLES, "USER"));
            dataProcessorFactory.processData(BaseConstants.USER_TYPE, userData,"");
            customUserDetailsService.reloadUsers();
            new GmailSender().sendMail("TEST_THEME", "your password = "+ pass,"oleksandr.perebeinis@gmail.com");

        }

        model.addAttribute(BaseConstants.REGISTRATION_TITLE, messageSource.getMessage(BaseConstants.REGISTRATION_TITLE, new Object[]{""}, locale));
        model.addAttribute(BaseConstants.FIRST_NAME, messageSource.getMessage(BaseConstants.TITLE_FIRST_NAME, new Object[]{""}, locale));
        model.addAttribute(BaseConstants.LAST_NAME, messageSource.getMessage(BaseConstants.TITLE_LAST_NAME, new Object[]{""}, locale));
        model.addAttribute(BaseConstants.USER_EMAIL, messageSource.getMessage(BaseConstants.TITLE_EMAIL, new Object[]{""}, locale));
        return "registration-new-user";
    }



    @RequestMapping("/login-error")
    public String loginError(ModelMap model, Locale locale) {
        model.addAttribute(BaseConstants.TAB_TITLE, messageSource.getMessage(BaseConstants.APP_TITLE, new Object[]{""}, locale));
        model.addAttribute(BaseConstants.SIGN_IN_TITLE, messageSource.getMessage(BaseConstants.LOGIN_MSG_TITLE, new Object[]{""}, locale));
        model.addAttribute(BaseConstants.PLACE_HOLDER_USERNAME, messageSource.getMessage(BaseConstants.PLACE_HOLDER_USERNAME, new Object[]{""}, locale));
        model.addAttribute(BaseConstants.PLACE_HOLDER_PASS, messageSource.getMessage(BaseConstants.PLACE_HOLDER_PASS, new Object[]{""}, locale));
        model.addAttribute(BaseConstants.SIGN_IN, messageSource.getMessage(BaseConstants.SIGN_IN, new Object[]{""}, locale));
        model.addAttribute(BaseConstants.REGISTER, messageSource.getMessage(BaseConstants.REGISTER, new Object[]{""}, locale));
        model.addAttribute(BaseConstants.LOGIN_ERROR, true);
        return "login";
    }


    @RequestMapping("/403")
    public String forbidden() {
        return "403";
    }


    private JSONObject createElementData(String elementName, String elementValue){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(BaseConstants.DATA, elementValue);
        jsonObject.put(BaseConstants.TYPE, BaseConstants.TEXT);
        jsonObject.put(BaseConstants.NAME, elementName);
        return jsonObject;
    }

}

package com.tracker.controller;

import com.tracker.constants.BaseConstants;
import com.tracker.dao.process.data.DataProcessorFactory;
import com.tracker.mail.send.impl.GmailSender;
import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
        model.addAttribute("newUser", new NewUser());
        return "registration-new-user";
    }

    @RequestMapping(value = "/change-my-pass",  method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> changeMyPass(@RequestBody String postData, Authentication authentication, ModelMap model) {
        model.addAttribute("newUser", new NewUser());
        JSONObject result = new JSONObject();
        try {
            String encodeURL = URLDecoder.decode(postData, BaseConstants.DEFAULT_ENCODING);
            JSONObject formData = new JSONObject(encodeURL);
            String newPass = formData.getString(BaseConstants.PASSWORD);
            String userEmail =  customUserDetailsService.loadUserDataByUsername(authentication.getName()).getAllUserData().getString(BaseConstants.EMAIL);
            new GmailSender().sendMail("TRACKER", "your password = "+ newPass, userEmail);
            return new ResponseEntity<Object>(HttpStatus.OK, HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            System.out.println("error");
        }
        return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "/registration-new-user",  method = RequestMethod.POST)
    public String registrationPost(Locale locale, ModelMap model, @ModelAttribute NewUser newUser) {
        String userName = newUser.getUsername();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
        if(userDetails != null){
            model.addAttribute(BaseConstants.USER_ALREADY_EXIST_IN_SYSTEM, messageSource.getMessage(BaseConstants.USER_ALREADY_EXIST_IN_SYSTEM, new Object[]{""}, locale));
        } else {
            JSONArray userData = new JSONArray();
            userData.put(createElementData(BaseConstants.USER_ID, userName));
            String userEmail = newUser.getUser_email();

            String randomUserPassword = RandomStringUtils.random(10, true, false);
            userData.put(createElementData(BaseConstants.USER_PASS, randomUserPassword));
            userData.put(createElementData(BaseConstants.FIRST_NAME,  newUser.getFirstName()));
            userData.put(createElementData(BaseConstants.LAST_NAME,  newUser.getLastName()));
            userData.put(createElementData(BaseConstants.EMAIL, newUser.getUser_email()));
            userData.put(createElementData(BaseConstants.USER_ROLES, "USER"));
            dataProcessorFactory.processData(BaseConstants.USER_TYPE, userData,"");
            customUserDetailsService.reloadUsers();
            new GmailSender().sendMail("TRACKER", "your password = "+ randomUserPassword, userEmail);

        }

        model.addAttribute("newUser", new NewUser());
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

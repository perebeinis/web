package com.tracker.controller;

import com.tracker.config.elements.user.registration.RegistrationUserCard;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.groups.ProcessUserGroups;
import com.tracker.dao.process.data.DataProcessorFactory;
import com.tracker.mail.factory.SendVelocityEmailFactory;
import com.tracker.mail.send.impl.GmailSender;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Locale;
import java.util.UUID;

@Controller
public class BaseAppElementsController {
    private static final String REGISTRATION_USER_CARD = "registrationUserCard";
    private static final String ALL_SYSTEM_GROUPS = "ALL_SYSTEM_GROUPS";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private SendVelocityEmailFactory sendVelocityEmailFactory;

    @Autowired
    private UserDetailsService customUserDetailsService;

    @Autowired
    private DataProcessorFactory dataProcessorFactory;

    @Autowired
    private ProcessUserGroups processUserGroups;

    @RequestMapping("/")
    public String root(Locale locale) {
        return "redirect:/search?filter=news";
    }

    @RequestMapping("/login")
    public String login(Locale locale, ModelMap model) {
        return "login";
    }


    @RequestMapping(value = "/registration-new-user", method = RequestMethod.GET)
    public String registration(Locale locale, ModelMap model) {
        model.addAttribute(REGISTRATION_USER_CARD, new RegistrationUserCard());
        model.addAttribute(ALL_SYSTEM_GROUPS, processUserGroups.getAllGroups());
        return "registration-new-user";
    }


    @RequestMapping(value = "/change-my-pass", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> changeMyPass(@RequestBody String postData, Authentication authentication, ModelMap model) {
        JSONObject result = new JSONObject();
        try {
            String encodeURL = URLDecoder.decode(postData, BaseConstants.DEFAULT_ENCODING);
            JSONObject formData = new JSONObject(encodeURL);
            String newPass = formData.getString(BaseConstants.PASSWORD);
            String userEmail = customUserDetailsService.loadUserDataByUsername(authentication.getName()).getAllUserData().getString(BaseConstants.EMAIL);
            JSONArray userData = new JSONArray();
            userData.put(createElementData(BaseConstants.USER_PASS, newPass));
            String currentUserId = customUserDetailsService.loadUserDataByUsername(authentication.getName()).getUserId().toString();
            dataProcessorFactory.processData(BaseConstants.USER_TYPE, userData, currentUserId);
            customUserDetailsService.reloadUsers();

            ModelMap modelMap = new ModelMap();
            modelMap.put(BaseConstants.USER_PASS, newPass);
            boolean messageSent = sendVelocityEmailFactory.sendVelocityEmail(BaseConstants.USER_CHANGE_PASS_VM, userEmail, modelMap);
            return new ResponseEntity<Object>(HttpStatus.OK, HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            System.out.println("error");
        }
        return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "/registration-new-user", method = RequestMethod.POST)
    public String registrationPost(Locale locale, ModelMap model, @Valid RegistrationUserCard registrationUserCard, BindingResult bindingResult) {
        String userName = registrationUserCard.getUsername();

        if (bindingResult.hasErrors()) {
            return "registration-new-user";
        }

        boolean userGroupAlreadyExist = processUserGroups.getAllGroups().containsKey(registrationUserCard.getUser_group());

        JSONArray userData = new JSONArray();
        userData.put(createElementData(BaseConstants.USER_ID, userName));
        String userEmail = registrationUserCard.getUser_email();


        String randomUserPassword = RandomStringUtils.random(10, true, false);
        userData.put(createElementData(BaseConstants.USER_PASS, randomUserPassword));
        userData.put(createElementData(BaseConstants.FIRST_NAME, registrationUserCard.getFirstName()));
        userData.put(createElementData(BaseConstants.LAST_NAME, registrationUserCard.getLastName()));
        userData.put(createElementData(BaseConstants.EMAIL, registrationUserCard.getUser_email()));
        String systemGroup = userGroupAlreadyExist ? BaseConstants.GROUP_USERS : BaseConstants.GROUP_ADMINS;
        userData.put(createElementData(BaseConstants.USER_ROLES, BaseConstants.DEFAULT_USER_ROLE+","+registrationUserCard.getUser_group()+","+systemGroup));
        dataProcessorFactory.processData(BaseConstants.USER_TYPE, userData, "");
        customUserDetailsService.reloadUsers();

        String currentUserId = customUserDetailsService.loadUserDataByUsername(userName).getUserId().toString();
        processUserGroups.processUserGroup(registrationUserCard.getUser_group(), userGroupAlreadyExist, currentUserId);

        ModelMap modelMap = new ModelMap();
        modelMap.put(BaseConstants.USERNAME, userName);
        modelMap.put(BaseConstants.USER_PASS, randomUserPassword);
        boolean messageSent = sendVelocityEmailFactory.sendVelocityEmail(BaseConstants.USER_REGISTRATION_VM, userEmail, modelMap);

        model.addAttribute(BaseConstants.REGISTERED_SUCCESSFUL, messageSent);
        return "login";
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


    private JSONObject createElementData(String elementName, String elementValue) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(BaseConstants.DATA, elementValue);
        jsonObject.put(BaseConstants.TYPE, BaseConstants.TEXT);
        jsonObject.put(BaseConstants.NAME, elementName);
        return jsonObject;
    }

}

package com.tracker.controller;

import com.mongodb.client.MongoDatabase;
import com.tracker.cards.user.UserCard;
import com.tracker.dao.UserData;
import com.tracker.dao.search.DataSearchFactory;
import com.tracker.dao.search.SearchDataModel;
import com.tracker.dynamic.FrontElementConfigurationParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.unbescape.html.HtmlEscape;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Application home page and login.
 */
@Controller
public class MainController {

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

    @RequestMapping("/")
    public String root(Locale locale) {
        return "redirect:/welcome";
    }

    /** Home page. */
    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String printWelcome(Locale locale, ModelMap model, Authentication authentication) {
        String welcome = messageSource.getMessage("bug-tracker.title", new Object[]{""}, locale);
        model.addAttribute("title", welcome);
        String loginMsg = messageSource.getMessage("loginMsg.title", new Object[]{""}, locale);
        model.addAttribute("loginMsg", loginMsg);
        model.addAttribute("headerList", frontElementConfigurationParser.parseHeaderMenuButtons(authentication));
        model.addAttribute("menuList", frontElementConfigurationParser.parseMenuButtons(authentication));

        UserData userData = new UserData();
        userData.getUserData(database);

        JSONObject jsonArray = userCard.getUserData();
        model.addAttribute("cardData", jsonArray);

        return "welcome";
    }


    /** Home page. */
    @RequestMapping(value = "/createNewUser", method = RequestMethod.GET)
    public String createNewUser(Locale locale, ModelMap model, Authentication authentication) {
        String welcome = messageSource.getMessage("bug-tracker.title", new Object[]{""}, locale);
        model.addAttribute("title", welcome);
        String loginMsg = messageSource.getMessage("loginMsg.title", new Object[]{""}, locale);
        model.addAttribute("loginMsg", loginMsg);
        model.addAttribute("headerList", frontElementConfigurationParser.parseHeaderMenuButtons(authentication));
        model.addAttribute("menuList", frontElementConfigurationParser.parseMenuButtons(authentication));

        UserData userData = new UserData();
        userData.getUserData(database);

        JSONObject jsonArray = userCard.getUserData();
        model.addAttribute("cardData", jsonArray);

        return "create-user-card";
    }

    @RequestMapping(value = "/search-data", method = RequestMethod.POST)
    public ResponseEntity<JSONArray> searchData(@RequestBody String searchData) {
        JSONArray jsonArray = new JSONArray();
        try {
            String encodeURL= URLDecoder.decode( searchData, "UTF-8" );
            JSONObject jsonObj = new JSONObject(encodeURL);
             jsonArray = dataSearchFactory.getData(jsonObj);
            System.out.println("*****");
        } catch (UnsupportedEncodingException e) {

        }
        System.out.println("aaaaaaaaaaaa");
        return new ResponseEntity<JSONArray>(jsonArray, HttpStatus.OK);
//        return  new ResponseEntity(dataSearchFactory.getData(new JSONObject()), HttpStatus.OK);
    }


    /** Home page. */
    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    /** User zone index. */
    @RequestMapping("/user/index")
    public String userIndex() {
        return "user/index";
    }

    /** Administration zone index. */
    @RequestMapping("/admin/index")
    public String adminIndex() {
        return "admin/index";
    }

    /** Shared zone index. */
    @RequestMapping("/shared/index")
    public String sharedIndex() {
        return "shared/index";
    }

    /** Login form. */
    @RequestMapping("/login")
    public String login(Locale locale, ModelMap model) {
        model.addAttribute("tabTitle", messageSource.getMessage("bug-tracker.title", new Object[]{""}, locale));
        model.addAttribute("signInTitle", messageSource.getMessage("loginMsg.title", new Object[]{""}, locale));
        model.addAttribute("placeholderUserName", messageSource.getMessage("placeholderUserName", new Object[]{""}, locale));
        model.addAttribute("placeholderPassword", messageSource.getMessage("placeholderPassword", new Object[]{""}, locale));
        model.addAttribute("signIn", messageSource.getMessage("signIn", new Object[]{""}, locale));
        return "login";
    }

    /** Login form with error. */
    @RequestMapping("/login-error")
    public String loginError(ModelMap model,Locale locale) {
        model.addAttribute("tabTitle", messageSource.getMessage("bug-tracker.title", new Object[]{""}, locale));
        model.addAttribute("signInTitle", messageSource.getMessage("loginMsg.title", new Object[]{""}, locale));
        model.addAttribute("placeholderUserName", messageSource.getMessage("placeholderUserName", new Object[]{""}, locale));
        model.addAttribute("placeholderPassword", messageSource.getMessage("placeholderPassword", new Object[]{""}, locale));
        model.addAttribute("signIn", messageSource.getMessage("signIn", new Object[]{""}, locale));
        model.addAttribute("loginError", true);
        return "login";
    }

    /** Simulation of an exception. */
    @RequestMapping("/simulateError")
    public void simulateError() {
        throw new RuntimeException("This is a simulated error message");
    }

    /** Error page. */
    @RequestMapping("/error")
    public String error(HttpServletRequest request, Model model) {
        model.addAttribute("errorCode", "Error " + request.getAttribute("javax.servlet.error.status_code"));
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("<ul>");
        while (throwable != null) {
            errorMessage.append("<li>").append(HtmlEscape.escapeHtml5(throwable.getMessage())).append("</li>");
            throwable = throwable.getCause();
        }
        errorMessage.append("</ul>");
        model.addAttribute("errorMessage", errorMessage.toString());
        return "error";
    }

    /** Error page. */
    @RequestMapping("/403")
    public String forbidden() {
        return "403";
    }


    @RequestMapping(value = "/create-new-user", method = RequestMethod.POST)
    public void createNewUser(HttpServletRequest request) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining());
        UserData userData = new UserData();
        userData.createNewUser();
    }

}

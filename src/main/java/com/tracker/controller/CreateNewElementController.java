package com.tracker.controller;

import com.mongodb.client.MongoDatabase;
import com.tracker.config.localization.MessageResolveService;
import com.tracker.constants.BaseConstants;
import com.tracker.controller.base.BaseControllerResponce;
import com.tracker.dao.create.DataCreator;
import com.tracker.dao.create.DataCreatorFactory;
import com.tracker.dao.search.DataSearchFactory;
import com.tracker.dynamic.FrontElementConfigurationParser;
import com.tracker.news.impl.NewsObserver;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by Perebeinis on 13.12.2017.
 */
@Controller
public class CreateNewElementController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private FrontElementConfigurationParser frontElementConfigurationParser;

    @Autowired
    private MongoDatabase database;

    @Autowired
    private NewsObserver newsObserver;

    @Autowired
    private DataCreatorFactory dataCreatorFactory;

    @Autowired
    private UserDetailsService customUserDetailsService;


    private static final String userType = "user";


    @RequestMapping(value = "/create-new-element", method = RequestMethod.POST , produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> searchData(@RequestBody String postData, @RequestParam("type") String type) {
        JSONObject result = new JSONObject();
        try {
            String encodeURL= URLDecoder.decode(postData, "UTF-8" );
            JSONArray formData = new JSONArray(encodeURL);
            System.out.println(formData);
            String newElementId = dataCreatorFactory.createData(type, formData);
            customUserDetailsService.reloadUsers();
            result.put(BaseConstants.DOCUMENT_ID,newElementId);
        } catch (UnsupportedEncodingException e) {
            System.out.println("error");
        }
        return new ResponseEntity<Object>(result.toString(), HttpStatus.OK);
    }
}

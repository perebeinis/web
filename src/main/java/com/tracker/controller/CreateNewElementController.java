package com.tracker.controller;

import com.mongodb.client.MongoDatabase;
import com.tracker.dao.create.DataCreator;
import com.tracker.dao.create.user.UserDataCreator;
import com.tracker.dao.search.DataSearchFactory;
import com.tracker.dynamic.FrontElementConfigurationParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private DataSearchFactory dataSearchFactory;

    private static final String userType = "user";


    @RequestMapping(value = "/create-new-element", method = RequestMethod.POST , produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> searchData(@RequestBody String postData, @RequestParam("type") String type) {
        JSONObject result = new JSONObject();
        try {
            String encodeURL= URLDecoder.decode(postData, "UTF-8" );
            JSONObject jsonObj = new JSONObject(encodeURL);
            System.out.println(jsonObj);
            createNewElement(type,jsonObj);
        } catch (UnsupportedEncodingException e) {
            System.out.println("error");
        }
        return new ResponseEntity<Object>(result.toString(), HttpStatus.OK);
    }

    private void createNewElement(String type, JSONObject incomingData){
        switch (type){
            case userType:
                DataCreator userDataCreator = new UserDataCreator();
                userDataCreator.createData(database, incomingData);

                break;
            default:
                break;

        }

    }

}

package com.tracker.controller;

import com.mongodb.client.MongoDatabase;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.process.data.DataProcessorFactory;
import com.tracker.observer.ChangingDataObserver;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Locale;

/**
 * Created by Perebeinis on 13.12.2017.
 */
@Controller
public class CreateNewElementController {
    @Autowired
    private ChangingDataObserver changingDataObserver;

    @Autowired
    private DataProcessorFactory dataProcessorFactory;

    @Autowired
    private UserDetailsService customUserDetailsService;

    @RequestMapping(value = "/create-new-element", method = RequestMethod.POST , produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> searchData(@RequestBody String postData, @RequestParam("type") String type) {
        JSONObject result = new JSONObject();
        try {
            String encodeURL= URLDecoder.decode(postData, "UTF-8" );
            JSONArray formData = new JSONArray(encodeURL);
            String newElementId = dataProcessorFactory.processData(type, formData,"");
            customUserDetailsService.reloadUsers();
            result.put(BaseConstants.DOCUMENT_ID,newElementId);
            changingDataObserver.createEvent(type, formData, newElementId);
        } catch (UnsupportedEncodingException e) {
            System.out.println("error");
        }
        return new ResponseEntity<Object>(result.toString(), HttpStatus.OK);
    }


    @RequestMapping(value = "/update-element", method = RequestMethod.POST , produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> updateElement(@RequestBody String postData, @RequestParam("type") String type, @RequestParam("id") String id) {
        JSONObject result = new JSONObject();
        try {
            String encodeURL= URLDecoder.decode(postData, "UTF-8" );
            JSONArray formData = new JSONArray(encodeURL);
            String newElementId = dataProcessorFactory.processData(type, formData, id);
            result.put(BaseConstants.DOCUMENT_ID, newElementId);
        } catch (UnsupportedEncodingException e) {
            System.out.println("error");
        }
        return new ResponseEntity<Object>(result.toString(), HttpStatus.OK);
    }

}

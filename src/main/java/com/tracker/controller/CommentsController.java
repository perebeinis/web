package com.tracker.controller;

import com.tracker.constants.BaseConstants;
import com.tracker.dao.process.data.DataProcessorFactory;
import com.tracker.mail.send.impl.GmailSender;
import com.tracker.view.elements.ViewElementsDataFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by Perebeinis on 02.04.2018.
 */
@Controller
public class CommentsController {
    @Autowired
    private DataProcessorFactory dataProcessorFactory;

    @Autowired
    private ViewElementsDataFactory viewElementsDataFactory;

    @RequestMapping(value = "/create-new-comment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> createNewComment(@RequestBody String postData,  ModelMap model, @RequestParam("type") String type) {
        JSONObject result = new JSONObject();
        try {
            String encodeURL = URLDecoder.decode(postData, BaseConstants.DEFAULT_ENCODING);
            JSONObject formData = new JSONObject(encodeURL);
            String newElementId = dataProcessorFactory.processData(BaseConstants.COMMENTS, formData, "");
            model = viewElementsDataFactory.getViewData(type, model, newElementId);
            JSONArray jsonArray = (JSONArray) ((JSONObject)model.get(BaseConstants.CARD_FILED_VALUES)).get(BaseConstants.COMMENTS);
            return new ResponseEntity<Object>(jsonArray.toString(), HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            System.out.println("error");
        }
        return new ResponseEntity<Object>(result.toString(), HttpStatus.OK);
    }
}

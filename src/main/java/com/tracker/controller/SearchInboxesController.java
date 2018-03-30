package com.tracker.controller;

import com.tracker.cards.CardDataProcessor;
import com.tracker.constants.BaseConstants;
import com.tracker.controller.base.BaseControllerResponse;
import com.tracker.dao.search.DataSearchFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Locale;

/**
 * Created by Perebeinis on 13.12.2017.
 */
@Controller
public class SearchInboxesController {
    @Autowired
    private BaseControllerResponse baseControllerResponse;

    @Autowired
    private DataSearchFactory dataSearchFactory;

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces =  MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String search(Locale locale, ModelMap model, Authentication authentication, @RequestParam("filter") String filter) {
        model = baseControllerResponse.getBaseResponseData(model,authentication, locale);
        model.addAttribute(BaseConstants.SEARCHERS, CardDataProcessor.getInstance().getFilterData(filter));
        return "search-data";
    }

    @RequestMapping(value = "/search-data", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> searchData(@RequestBody String searchData) {
        JSONObject result = new JSONObject();
        try {
            String encodeURL = URLDecoder.decode(searchData, BaseConstants.DEFAULT_ENCODING);
            JSONObject jsonObj = new JSONObject(encodeURL);
            result = dataSearchFactory.searchData(BaseConstants.ELEMENT, jsonObj);
        } catch (UnsupportedEncodingException e) {
            System.out.println("error");
        }
        return new ResponseEntity<Object>(result.toString(), HttpStatus.OK);
    }
}
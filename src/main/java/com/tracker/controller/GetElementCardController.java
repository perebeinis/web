package com.tracker.controller;

import com.tracker.config.localization.MessageResolveService;
import com.tracker.constants.BaseConstants;
import com.tracker.controller.base.BaseControllerResponse;
import com.tracker.dao.process.data.DataProcessorFactory;
import com.tracker.view.elements.ViewElementsDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;

@Controller
public class GetElementCardController {

    @Autowired
    private BaseControllerResponse baseControllerResponce;

    @Autowired
    private ViewElementsDataFactory viewElementsDataFactory;

    private static final String RESPONSE_ELEMENT_TYPE = "create-element-card";

    @RequestMapping(value = "/create-element", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getElementCard(Locale locale, ModelMap model, Authentication authentication, @RequestParam("type") String type) {
        model = baseControllerResponce.getBaseResponseData(model, authentication, locale);
        model = viewElementsDataFactory.getViewData(type, model, null);
        model.addAttribute(BaseConstants.MODE, BaseConstants.CREATE);
        model.addAttribute(BaseConstants.ELEMENT_TYPE, type);
        return RESPONSE_ELEMENT_TYPE;
    }

    @RequestMapping(value = "/get-element", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getElementCardExist(Locale locale, ModelMap model, Authentication authentication, @RequestParam("type") String type, @RequestParam("id") String id) {
        model = baseControllerResponce.getBaseResponseData(model, authentication, locale);
        model = viewElementsDataFactory.getViewData(type, model, id);
        model.addAttribute(BaseConstants.MODE, BaseConstants.MODE_VIEW);
        model.addAttribute(BaseConstants.ELEMENT_TYPE, type);
        return RESPONSE_ELEMENT_TYPE;
    }

}

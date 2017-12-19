package com.tracker.cards.impl;

import com.mongodb.client.MongoDatabase;
import com.tracker.cards.CardData;
import com.tracker.cards.CardDataProcessor;
import com.tracker.dao.search.AbstractDataSearch;
import com.tracker.dao.search.GetElementFactory;
import com.tracker.dynamic.FrontElementConfigurationParser;
import org.json.JSONObject;
import org.springframework.context.MessageSource;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;

import java.util.Properties;

public class UserCardData extends CardDataProcessor implements CardData{
    @Override
    public ModelMap getData(ModelMap model, String elementId, String elementType, MessageSource messageSource, FrontElementConfigurationParser frontElementConfigurationParser, MongoDatabase database, Properties pathsConfigProperties, GetElementFactory getElementFactory) {
//        String welcome = messageSource.getMessage("bug-tracker.title", new Object[]{""}, locale);
//        model.addAttribute("title", welcome);
//        String loginMsg = messageSource.getMessage("loginMsg.title", new Object[]{""}, locale);
//        model.addAttribute("loginMsg", loginMsg);
        setPathsConfigProperties(pathsConfigProperties);
        model.addAttribute(headerListConst, frontElementConfigurationParser.parseHeaderMenuButtons());
        model.addAttribute(menuListConst, frontElementConfigurationParser.parseMenuButtons());
        if(!StringUtils.isEmpty(elementType) && !StringUtils.isEmpty(elementId)){
            model.addAttribute(cardFiledValuesConst, getElementFactory.searchData(elementType, elementId));
        }

        JSONObject jsonArray = createCardData(elementType);
        model.addAttribute(cardDataConst, jsonArray);
        return model;
    }
}

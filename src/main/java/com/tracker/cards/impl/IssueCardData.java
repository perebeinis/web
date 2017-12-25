package com.tracker.cards.impl;

import com.mongodb.client.MongoDatabase;
import com.tracker.cards.CardData;
import com.tracker.cards.CardDataProcessor;
import com.tracker.dao.search.GetElementFactory;
import com.tracker.dynamic.FrontElementConfigurationParser;
import org.json.JSONObject;
import org.springframework.context.MessageSource;
import org.springframework.ui.ModelMap;

import java.util.Properties;

/**
 * Created by Perebeinis on 19.12.2017.
 */
public class IssueCardData extends CardDataProcessor implements CardData{
    @Override
    public ModelMap getData(ModelMap model, String elementId,  String elementType, MessageSource messageSource, FrontElementConfigurationParser frontElementConfigurationParser, MongoDatabase database, Properties pathsConfigProperties,GetElementFactory getElementFactory) {
        setPathsConfigProperties(pathsConfigProperties);
        model.addAttribute(headerListConst, frontElementConfigurationParser.parseHeaderMenuButtons());
        model.addAttribute(menuListConst, frontElementConfigurationParser.parseMenuButtons());

        JSONObject jsonArray = createCardData(elementType);
        model.addAttribute(cardDataConst, jsonArray);
        return model;
    }
}

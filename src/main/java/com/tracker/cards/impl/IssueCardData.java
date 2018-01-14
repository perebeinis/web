package com.tracker.cards.impl;

import com.mongodb.client.MongoDatabase;
import com.tracker.cards.CardData;
import com.tracker.cards.CardDataProcessor;
import com.tracker.dao.search.DataSearchFactory;
import com.tracker.dynamic.FrontElementConfigurationParser;
import org.json.JSONObject;
import org.springframework.context.MessageSource;
import org.springframework.ui.ModelMap;

import java.util.Properties;

/**
 * Created by Perebeinis on 19.12.2017.
 */
public class IssueCardData implements CardData{
    @Override
    public ModelMap getData(ModelMap model, String elementId,  String elementType, MessageSource messageSource, FrontElementConfigurationParser frontElementConfigurationParser, MongoDatabase database, Properties pathsConfigProperties,DataSearchFactory getElementFactory) {
        model.addAttribute(headerListConst, frontElementConfigurationParser.parseHeaderMenuButtons());
        model.addAttribute(menuListConst, frontElementConfigurationParser.parseMenuButtons());

        JSONObject jsonArray =  CardDataProcessor.getInstance().getCardDataForElementType(elementType);
        model.addAttribute(cardDataConst, jsonArray);
        return model;
    }
}

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

public class UserCardData implements CardData{
    @Override
    public ModelMap getData(ModelMap model, String elementId, String elementType, MessageSource messageSource, FrontElementConfigurationParser frontElementConfigurationParser, MongoDatabase database, Properties pathsConfigProperties, GetElementFactory getElementFactory) {
        if(!StringUtils.isEmpty(elementType) && !StringUtils.isEmpty(elementId)){
            model.addAttribute(cardFiledValuesConst, getElementFactory.searchData(elementType, elementId));
        }else{
            model.addAttribute(cardFiledValuesConst, new JSONObject());
        }

        JSONObject jsonArray = CardDataProcessor.getInstance().getCardDataForElementType(elementType);
        model.addAttribute(cardDataConst, jsonArray);
        return model;
    }
}

package com.tracker.cards;

import com.mongodb.client.MongoDatabase;
import com.tracker.dao.search.GetElementFactory;
import com.tracker.dynamic.FrontElementConfigurationParser;
import org.springframework.context.MessageSource;
import org.springframework.ui.ModelMap;

import java.util.Properties;

public interface CardData {

    final String headerListConst = "headerList";
    final String menuListConst = "menuList";
    final String cardDataConst = "cardData";
    final String cardFiledValuesConst = "cardFiledValues";

    ModelMap getData(ModelMap model, String elementId, String elementType, MessageSource messageSource, FrontElementConfigurationParser frontElementConfigurationParser, MongoDatabase database, Properties pathsConfigProperties, GetElementFactory getElementFactory);
}

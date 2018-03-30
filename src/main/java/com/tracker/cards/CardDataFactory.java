package com.tracker.cards;

import com.mongodb.client.MongoDatabase;
import com.tracker.cards.impl.DefaultCardData;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.search.DataSearchFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.ModelMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

public class CardDataFactory {

    @Autowired
    protected MessageSource messageSource;

    @Autowired
    protected MongoDatabase database;

    @Autowired
    protected Properties pathsConfigProperties;

    @Autowired
    private DataSearchFactory getElementFactory;

    final static Map<String, Supplier<CardData>> map = new HashMap<>();
    static {
        map.put(BaseConstants.DEFAULT, DefaultCardData::new);
    }

    public ModelMap getCardData(String elementType, ModelMap model, String elementId){
        Supplier<CardData> element = map.get(elementType);
        if(element != null) {
            return element.get().getData(model, elementId, elementType, getElementFactory);
        }else{
            return map.get(BaseConstants.DEFAULT).get().getData(model, elementId, elementType, getElementFactory);
        }
    }
}

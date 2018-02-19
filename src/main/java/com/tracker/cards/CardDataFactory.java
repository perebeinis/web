package com.tracker.cards;

import com.mongodb.client.MongoDatabase;
import com.tracker.cards.impl.DefaultCardData;
import com.tracker.cards.impl.IssueCardData;
import com.tracker.cards.impl.UserCardData;
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
    private static final String DEFAULT = "default";

    final static Map<String, Supplier<CardData>> map = new HashMap<>();
    static {
//        map.put("user", UserCardData::new);
//        map.put("issue", IssueCardData::new);
        map.put("default", DefaultCardData::new);
    }

    public ModelMap getCardData(String elementType, ModelMap model, String elementId){
        Supplier<CardData> element = map.get(elementType);
        if(element != null) {
            return element.get().getData(model, elementId, elementType, getElementFactory);
        }else{
            return map.get(DEFAULT).get().getData(model, elementId, elementType, getElementFactory);
        }
    }
}

package com.tracker.cards;

import com.tracker.cards.impl.UserCardData;
import org.springframework.ui.ModelMap;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CardDataFactory {

    final static Map<String, Supplier<CardData>> map = new HashMap<>();
    static {
        map.put("user", UserCardData::new);
    }

    public ModelMap getCardData(String elementType, ModelMap model, String elementId){
        Supplier<CardData> element = map.get(elementType);
        if(element != null) {
            return element.get().getData(model, elementId);
        }
        throw new IllegalArgumentException("No such shape " + elementType);
    }
}

package com.tracker.cards.impl;

import com.tracker.cards.CardData;
import com.tracker.cards.CardDataProcessor;
import com.tracker.dao.search.DataSearchFactory;
import org.json.JSONObject;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;

public class IssueCardData implements CardData{
    @Override
    public ModelMap getData(ModelMap model, String elementId,  String elementType, DataSearchFactory getElementFactory) {
        if(!StringUtils.isEmpty(elementType) && !StringUtils.isEmpty(elementId)){
            model.addAttribute(cardFiledValuesConst, getElementFactory.searchDataById(elementType, elementId));
        }else{
            model.addAttribute(cardFiledValuesConst, new JSONObject());
        }

        JSONObject jsonArray = CardDataProcessor.getInstance().getCardDataForElementType(elementType);
        model.addAttribute(cardDataConst, jsonArray);
        return model;
    }
}

package com.tracker.cards.impl;

import com.tracker.cards.CardData;
import com.tracker.cards.CardDataProcessor;
import com.tracker.dao.search.AbstractDataSearch;
import org.json.JSONObject;
import org.springframework.ui.ModelMap;

public class UserCardData extends CardDataProcessor implements CardData{
    @Override
    public ModelMap getData(ModelMap model, String elementId) {
//        String welcome = messageSource.getMessage("bug-tracker.title", new Object[]{""}, locale);
//        model.addAttribute("title", welcome);
//        String loginMsg = messageSource.getMessage("loginMsg.title", new Object[]{""}, locale);
//        model.addAttribute("loginMsg", loginMsg);
        model.addAttribute("headerList", frontElementConfigurationParser.parseHeaderMenuButtons());
        model.addAttribute("menuList", frontElementConfigurationParser.parseMenuButtons());

//        JSONObject jsonArray = userCard.getUserData();
//        model.addAttribute("cardData", jsonArray);
        return model;
    }
}

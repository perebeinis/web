package com.tracker.cards;

import org.springframework.ui.ModelMap;

public interface CardData {

    ModelMap getData(ModelMap model, String elementId);
}

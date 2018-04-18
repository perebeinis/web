package com.tracker.view.elements;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.ui.ModelMap;

public interface ViewElementsData {

    final String headerListConst = "headerList";
    final String menuListConst = "menuList";
    final String cardDataConst = "cardData";
    final String cardFiledValuesConst = "cardFiledValues";
    final String PROPERTIES = "properties";
    final String TABS = "tabs";
    final String JSON = "json";
    final String ROLE_ = "ROLE_";
    final String WINDOW_ELEMENTS_PATH_CONSTANT = "window-elements-path";
    final String CARD_ELEMENTS_PATH_CONSTANT = "card-elements-path";
    final String ENABLE_FOR_USER_ROLES_CONSTANT = "enableForRoles";
    final String ELEMENTS = "elements";

    ModelMap getData(ModelMap model, String elementId, String elementType);
    JSONArray getWindowItemsForUser(String elementType);
    JSONObject getFilterData(String filterName);
}

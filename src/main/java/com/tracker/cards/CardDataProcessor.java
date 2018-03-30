package com.tracker.cards;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoDatabase;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.search.DataSearchFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class CardDataProcessor {

    @Autowired
    protected MessageSource messageSource;

    @Autowired
    protected MongoDatabase database;

    @Autowired
    protected Properties pathsConfigProperties;

    @Autowired
    private DataSearchFactory getElementFactory;

    private static final String PROPERTIES = "properties";
    private static final String TABS = "tabs";
    private static final String JSON = "json";
    private static final String ROLE_ = "ROLE_";
    private static final String HEADER_ELEMENT_CONSTANT = "header-element";
    private static final String MENU_ELEMENT_CONSTANT = "menu-element";
    private static final String ENABLE_FOR_USER_ROLES_CONSTANT = "enableForRoles";
    private static final String ELEMENTS = "elements";
    private static Map<String, JSONObject> cardElements = new HashMap<>();
    private Map<String,JSONObject> filterData  = new HashMap<>();

    private static CardDataProcessor instance;

    public static CardDataProcessor getInstance() {
        if (instance == null) {
            synchronized (CardDataProcessor.class) {
                if (instance == null) {
                    instance = new CardDataProcessor();
                }
            }
        }
        return instance;
    }

    public CardDataProcessor() {
        createCardData();
    }

    public JSONObject createCardData() {
        JSONObject JSONObject = new JSONObject();
        if (pathsConfigProperties != null) {
            try {
                Enumeration propertyNames = pathsConfigProperties.propertyNames();

                while (propertyNames.hasMoreElements()) {
                    String key = (String) propertyNames.nextElement();
                    String path = pathsConfigProperties.getProperty(key);
                        try (InputStream inputStream = this.getClass().getResourceAsStream(path)) {
                            JsonParser parser = new JsonParser();
                            JsonElement jsonElement = parser.parse(new InputStreamReader(inputStream));
                            JSONObject configurationData = new JSONObject(jsonElement.toString());
                            cardElements.put(key, configurationData);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
                return JSONObject;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return JSONObject;
    }

    public JSONObject getCardDataForElementType(String elementType) {
        if (cardElements.size() == 0) {
            synchronized (CardDataProcessor.class) {
                if (cardElements.size() == 0) {
                    createCardData();
                }
            }
        }
        return cardElements.get(elementType);
    }

    public JSONArray getCardAttributes(String elementType) {
        JSONArray jsonArray = new JSONArray();
        ((JSONArray) cardElements.get(elementType).get(TABS)).forEach((tab) -> {
            for (Object property : ((JSONArray) (((JSONObject) tab).get(PROPERTIES)))) {
                jsonArray.put(property);
            }
        });
        return jsonArray;
    }

    private boolean containsAnyRole(Authentication authentication, String[] userRoles) {
        boolean containRole = false;
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            for (String userRole : userRoles) {
                if (grantedAuthority.getAuthority().replace(ROLE_, "").equals(userRole)) {
                    containRole = true;
                    break;
                }
            }

        }
        return containRole;
    }

    public JSONArray getMenuItemsForUser(String elementType) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JSONArray jsonArray = new JSONArray(((JSONArray) cardElements.get(elementType).get(ELEMENTS)).toString());
        jsonArray = loopData(jsonArray, authentication);
        return jsonArray;
    }

    private JSONArray loopData(JSONArray jsonArray, Authentication authentication) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            String enableForRoles = jsonObject.get(ENABLE_FOR_USER_ROLES_CONSTANT) != null ? (String) jsonObject.get(ENABLE_FOR_USER_ROLES_CONSTANT) : "";
            filterData.put((String) jsonObject.get(BaseConstants.NAME), jsonObject);
            if (!StringUtils.isEmpty(enableForRoles) && !containsAnyRole(authentication, enableForRoles.split(","))) {
                jsonArray.remove(i);
            } else {
                try {
                    if (jsonObject.get(ELEMENTS) != null) {
                        loopData((JSONArray) jsonObject.get(ELEMENTS), authentication);
                    }
                } catch (Exception e) {}
            }
        }
        return jsonArray;
    }

    public JSONObject getFilterData(String filterName) {
        return filterData.get(filterName);
    }

    public void setPathsConfigProperties(PropertiesFactoryBean pathsConfigProperties) throws IOException {
        this.pathsConfigProperties = pathsConfigProperties.getObject();
    }
}

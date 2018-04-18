package com.tracker.view.elements.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.tracker.constants.BaseConstants;
import com.tracker.dao.search.execute.DataSearchFactory;
import com.tracker.view.elements.ViewElementsData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.*;

public class DefaultViewElementsData implements ViewElementsData {

    private Properties pathsConfigProperties;

    @Autowired
    private DataSearchFactory dataSearchFactory;


    private static Map<String, JSONObject> viewElements = new HashMap<>();
    private Map<String, JSONObject> filterData = new HashMap<>();


    private static DefaultViewElementsData instance;

    public static DefaultViewElementsData getInstance() {
        if (instance == null) {
            synchronized (DefaultViewElementsData.class) {
                if (instance == null) {
                    instance = new DefaultViewElementsData();
                }
            }
        }
        return instance;
    }


    @Override
    public ModelMap getData(ModelMap model, String elementId, String elementType) {
        if (!StringUtils.isEmpty(elementType) && !StringUtils.isEmpty(elementId)) {
            model.addAttribute(cardFiledValuesConst, dataSearchFactory.searchDataById(elementType, elementId));
        } else {
            model.addAttribute(cardFiledValuesConst, new JSONObject());
        }

        JSONObject jsonArray = getCardDataForElementType(elementType);
        model.addAttribute(cardDataConst, jsonArray);
        return model;
    }


    public DefaultViewElementsData() {
        createCardData();
    }

    public JSONObject createCardData() {
        JSONObject JSONObject = new JSONObject();
        if (pathsConfigProperties != null) {
            try {
                List<String> paths = new ArrayList<>();
                paths.add(pathsConfigProperties.getProperty(WINDOW_ELEMENTS_PATH_CONSTANT));
                paths.add(pathsConfigProperties.getProperty(CARD_ELEMENTS_PATH_CONSTANT));

                for (String path : paths) {
                    URL packageURL = this.getClass().getClassLoader().getResource(path);
                    if (packageURL != null) {
                        URI uri = new URI(packageURL.toString());
                        File folder = new File(uri.getPath());
                        File[] filesList = folder.listFiles();
                        if (filesList != null) {
                            for (File file : filesList) {
                                try (Reader reader = new InputStreamReader(new FileInputStream(file));) {
                                    JsonParser parser = new JsonParser();
                                    JsonElement jsonElement = parser.parse(reader);
                                    JSONObject configurationData = new JSONObject(jsonElement.toString());
                                    viewElements.put(file.getName().toString().replace(BaseConstants.JSON_EXTENSION, ""), configurationData);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            System.out.println("File list is empty for path = " + path);
                        }
                    } else {
                        System.out.println("packageURL is empty for path = " + path);
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
        if (viewElements.size() == 0) {
            synchronized (DefaultViewElementsData.class) {
                if (viewElements.size() == 0) {
                    createCardData();
                }
            }
        }
        return viewElements.get(elementType);
    }

    public JSONArray getCardAttributes(String elementType) {
        JSONArray jsonArray = new JSONArray();
        if (viewElements.size() > 0) {
            ((JSONArray) viewElements.get(elementType).get(TABS)).forEach((tab) -> {
                for (Object property : ((JSONArray) (((JSONObject) tab).get(PROPERTIES)))) {
                    jsonArray.put(property);
                }

                jsonArray.put(addDefaultParams());
            });
        }
        return jsonArray;
    }

    private JSONObject addDefaultParams() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(BaseConstants.NAME, BaseConstants.CREATOR);
        jsonObject.put(BaseConstants.TYPES_FOR_SAVING, BaseConstants.CREATOR);
        return jsonObject;
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

    @Override
    public JSONArray getWindowItemsForUser(String elementType) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JSONArray jsonArray = new JSONArray((viewElements.get(elementType).get(ELEMENTS)).toString());
        jsonArray = processWindowItemsForUser(jsonArray, authentication);
        return jsonArray;
    }

    private JSONArray processWindowItemsForUser(JSONArray jsonArray, Authentication authentication) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            String enableForRoles = jsonObject.get(ENABLE_FOR_USER_ROLES_CONSTANT) != null ? (String) jsonObject.get(ENABLE_FOR_USER_ROLES_CONSTANT) : "";
            filterData.put((String) jsonObject.get(BaseConstants.NAME), jsonObject);
            if (!StringUtils.isEmpty(enableForRoles) && !containsAnyRole(authentication, enableForRoles.split(","))) {
                jsonArray.remove(i);
            } else {
                try {
                    if (jsonObject.get(ELEMENTS) != null) {
                        processWindowItemsForUser((JSONArray) jsonObject.get(ELEMENTS), authentication);
                    }
                } catch (Exception e) {
//                    System.out.println("error in processWindowItemsForUser");
                }
            }
        }
        return jsonArray;
    }

    @Override
    public JSONObject getFilterData(String filterName) {
        return filterData.get(filterName);
    }

    public void setPathsConfigProperties(PropertiesFactoryBean pathsConfigProperties) throws IOException {
        this.pathsConfigProperties = pathsConfigProperties.getObject();
    }


}

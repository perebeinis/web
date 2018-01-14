package com.tracker.dynamic;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class FrontElementConfigurationParser {

    @Autowired
    private MessageSource messageSource;


    private Properties pathsConfigProperties;
    private Map<String,JSONArray> filterSearchers = null;
    private Map<String,JSONObject> filterData = null;


    private static final String EMPTY_ATTRIBUTE_CONSTANT = "#text";
    private static final String ELEMENTS_CONSTANT = "elements";
    private static final String HEADER_ELEMENT_CONSTANT = "header-element";
    private static final String MENU_ELEMENT_CONSTANT = "menu-element";
    private static final String ENABLE_FOR_USER_ROLES_CONSTANT = "enableForRoles";
    private static final String SEARCH_PARAMS_CONSTANT = "searchParams";
    private static final String TITLE_CONSTANT = "title";
    private static final String TYPE_CONSTANT = "type";
    private static final String NAME_CONSTANT = "name";
    private static final String SEARCH_COLUMNS = "searchColumns";
    private static final String SEARCHERS = "searchers";
    private static final String SEARCHER_CONST = "searcher";
    private static final String SUB_ELEMENTS = "subElements";
    private static final String FILTER_DATA = "filterData";



    public JSONArray parseMenuButtons(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            JSONArray menuElementsList = new JSONArray();
            InputStream inputStream = this.getClass().getResourceAsStream(pathsConfigProperties.getProperty(MENU_ELEMENT_CONSTANT));
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName(MENU_ELEMENT_CONSTANT);
            filterSearchers = new HashMap<>();
            filterData = new HashMap<>();
            for (int i = 0; i < nodeList.getLength(); i++){
                Node headerElementNode = nodeList.item(i);
                String headerElementName = headerElementNode.getAttributes().getNamedItem(NAME_CONSTANT).getTextContent();
                String title = headerElementNode.getAttributes().getNamedItem(TITLE_CONSTANT).getTextContent();
                String type = headerElementNode.getAttributes().getNamedItem(TYPE_CONSTANT).getTextContent();
                String userRoles = headerElementNode.getAttributes().getNamedItem(ENABLE_FOR_USER_ROLES_CONSTANT).getTextContent();
                String searchParams = headerElementNode.getAttributes().getNamedItem(SEARCH_PARAMS_CONSTANT) == null? "{}" : headerElementNode.getAttributes().getNamedItem(SEARCH_PARAMS_CONSTANT).getTextContent();
                String searchColumns = headerElementNode.getAttributes().getNamedItem(SEARCH_COLUMNS) == null? "" : headerElementNode.getAttributes().getNamedItem(SEARCH_COLUMNS).getTextContent();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put(NAME_CONSTANT,headerElementName);
                jsonObject.put(TITLE_CONSTANT,title);
                jsonObject.put(ENABLE_FOR_USER_ROLES_CONSTANT,userRoles);
                jsonObject.put(SEARCH_PARAMS_CONSTANT, searchParams);
                jsonObject.put(SEARCH_COLUMNS, searchColumns);
                jsonObject.put(TYPE_CONSTANT, type);
                jsonObject.put(SEARCHERS,getSubElements(headerElementNode, SEARCHER_CONST));
                filterSearchers.put(headerElementName, getSubElements(headerElementNode, SEARCHER_CONST));
                jsonObject.put(SUB_ELEMENTS, new JSONArray());
                filterData.put(headerElementName, jsonObject);


                // Get sub searchers
                NodeList nodeAttributesList = headerElementNode.getChildNodes();
                JSONArray attributeList = new JSONArray();
                for (int j = 0; j < nodeAttributesList.getLength(); j++){
                    Node attributeElementNode = nodeAttributesList.item(j);
                    if(attributeElementNode.getAttributes()!=null){
                        String elementNameSub = attributeElementNode.getAttributes().getNamedItem(NAME_CONSTANT).getTextContent();
                        String titleSub = attributeElementNode.getAttributes().getNamedItem(TITLE_CONSTANT).getTextContent();
                        String typeSub = attributeElementNode.getAttributes().getNamedItem(TYPE_CONSTANT).getTextContent();
                        String subSearchColumns = attributeElementNode.getAttributes().getNamedItem(SEARCH_COLUMNS) == null? "" : attributeElementNode.getAttributes().getNamedItem(SEARCH_COLUMNS).getTextContent();
                        String searchParamsData = attributeElementNode.getAttributes().getNamedItem(SEARCH_PARAMS_CONSTANT) == null? "{}" : attributeElementNode.getAttributes().getNamedItem(SEARCH_PARAMS_CONSTANT).getTextContent();

                        JSONObject jsonObjectSub = new JSONObject();
                        jsonObjectSub.put(NAME_CONSTANT,elementNameSub);
                        jsonObjectSub.put(TITLE_CONSTANT,titleSub);
                        jsonObjectSub.put(TYPE_CONSTANT,typeSub);
                        jsonObjectSub.put(SEARCH_PARAMS_CONSTANT, searchParamsData);
                        jsonObjectSub.put(SEARCH_COLUMNS,subSearchColumns);
                        jsonObjectSub.put(SEARCHERS,getSubElements(attributeElementNode, SEARCHER_CONST));
                        filterSearchers.put(elementNameSub,getSubElements(attributeElementNode, SEARCHER_CONST));
                        ((JSONArray)jsonObject.get(SUB_ELEMENTS)).put(jsonObjectSub);
                        filterData.put(elementNameSub, jsonObjectSub);

                    }
                }
                menuElementsList.put(jsonObject);

            }
            return menuElementsList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    private JSONArray getSubElements(Node headerElementNode, String elementType){
        NodeList nodeAttributesList = headerElementNode.getChildNodes();
        JSONArray attributeList = new JSONArray();
        for (int j = 0; j < nodeAttributesList.getLength(); j++){
            Node attributeElementNode = nodeAttributesList.item(j);
            if(attributeElementNode.getAttributes()!=null && attributeElementNode.getNodeName().equals(elementType)){
                String elementNameSub = attributeElementNode.getAttributes().getNamedItem(NAME_CONSTANT).getTextContent();
                String titleSub = attributeElementNode.getAttributes().getNamedItem(TITLE_CONSTANT).getTextContent();
                String typeSub = attributeElementNode.getAttributes().getNamedItem(TYPE_CONSTANT).getTextContent();
                String subSearchColumns = attributeElementNode.getAttributes().getNamedItem(SEARCH_COLUMNS) == null? "" : attributeElementNode.getAttributes().getNamedItem(SEARCH_COLUMNS).getTextContent();


                JSONObject jsonObjectSub = new JSONObject();
                jsonObjectSub.put(NAME_CONSTANT,elementNameSub);
                jsonObjectSub.put(TITLE_CONSTANT,titleSub);
                jsonObjectSub.put(TYPE_CONSTANT,typeSub);
                jsonObjectSub.put(SEARCH_COLUMNS,subSearchColumns);
                attributeList.put(jsonObjectSub);

            }
        }

        return attributeList;
    }


    public JSONObject getFilterData(String filterName){
        try {
            if(filterData == null){
                parseMenuButtons();
                return filterData.get(filterName);
            }else{
                return filterData.get(filterName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }


    public JSONArray parseHeaderMenuButtons(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            JSONArray attributeList = new JSONArray();
            InputStream inputStream = this.getClass().getResourceAsStream(pathsConfigProperties.getProperty(HEADER_ELEMENT_CONSTANT));
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName(HEADER_ELEMENT_CONSTANT);
            for (int i = 0; i < nodeList.getLength(); i++){
                Node headerElementNode = nodeList.item(i);
                String headerElementName = headerElementNode.getAttributes().getNamedItem(NAME_CONSTANT).getTextContent();
                String title = headerElementNode.getAttributes().getNamedItem(TITLE_CONSTANT)!= null ?  headerElementNode.getAttributes().getNamedItem(TITLE_CONSTANT).getTextContent() : "";
                String type =  headerElementNode.getAttributes().getNamedItem(TYPE_CONSTANT)!=null ? headerElementNode.getAttributes().getNamedItem(TYPE_CONSTANT).getTextContent() : "";
                String userRoles = headerElementNode.getAttributes().getNamedItem(ENABLE_FOR_USER_ROLES_CONSTANT).getTextContent();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put(NAME_CONSTANT,headerElementName);
                jsonObject.put(TITLE_CONSTANT,title);
                jsonObject.put(ENABLE_FOR_USER_ROLES_CONSTANT,userRoles);
                jsonObject.put(TYPE_CONSTANT, type);
                jsonObject.put(SUB_ELEMENTS, new JSONArray());
                attributeList.put(jsonObject);

                    NodeList nodeAttributesList = headerElementNode.getChildNodes();

                    for (int j = 0; j < nodeAttributesList.getLength(); j++){
                        Node attributeElementNode = nodeAttributesList.item(j);
                        if(attributeElementNode.getAttributes()!=null){
                            String elementNameSub = attributeElementNode.getAttributes().getNamedItem(NAME_CONSTANT).getTextContent();
                            String titleSub = attributeElementNode.getAttributes().getNamedItem(TITLE_CONSTANT).getTextContent();

                            JSONObject jsonObjectSub = new JSONObject();
                            jsonObjectSub.put(NAME_CONSTANT,elementNameSub);
                            jsonObjectSub.put(TITLE_CONSTANT,titleSub);
                            ((JSONArray) jsonObject.get(SUB_ELEMENTS)).put(jsonObjectSub);

                        }
                    }

            }
            return attributeList;

        } catch (Exception e) {
            e.printStackTrace();
        }
       return new JSONArray();
    }

    private boolean containsAny(Authentication authentication, String[] userRoles){
        boolean containRole =false;
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            for (String userRole : userRoles) {
                if(grantedAuthority.getAuthority().replace("ROLE_","").equals(userRole)){
                    containRole = true;
                    break;
                }
            }

        }
        return containRole;
    }

    public void setPathsConfigProperties(PropertiesFactoryBean pathsConfigProperties) throws IOException {
        this.pathsConfigProperties = pathsConfigProperties.getObject();
    }
}

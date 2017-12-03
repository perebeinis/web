package com.tracker.dynamic;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.Properties;

public class FrontElementConfigurationParser {

    @Autowired
    private Properties pathsConfigProperties;
    private static final String EMPTY_ATTRIBUTE_CONSTANT = "#text";
    private static final String ELEMENTS_CONSTANT = "elements";
    private static final String HEADER_ELEMENT_CONSTANT = "header-element";
    private static final String MENU_ELEMENT_CONSTANT = "menu-element";
    private static final String ENABLE_FOR_USER_ROLES_CONSTANT = "enableForRoles";
    private static final String SEARCH_PARAMS_CONSTANT = "searchParams";
    private static final String TITLE_CONSTANT = "title";
    private static final String NAME_CONSTANT = "name";



    public JSONArray parseMenuButtons(Authentication authentication){
        try {
            JSONArray menuElementsList = new JSONArray();
            InputStream inputStream = this.getClass().getResourceAsStream(pathsConfigProperties.getProperty(MENU_ELEMENT_CONSTANT));
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName(MENU_ELEMENT_CONSTANT);
            for (int i = 0; i < nodeList.getLength(); i++){
                Node headerElementNode = nodeList.item(i);
                String headerElementName = headerElementNode.getAttributes().getNamedItem(NAME_CONSTANT).getTextContent();
                String title = headerElementNode.getAttributes().getNamedItem(TITLE_CONSTANT).getTextContent();
                String userRoles = headerElementNode.getAttributes().getNamedItem(ENABLE_FOR_USER_ROLES_CONSTANT).getTextContent();
                String searchParams = headerElementNode.getAttributes().getNamedItem(SEARCH_PARAMS_CONSTANT) == null? "" : headerElementNode.getAttributes().getNamedItem(SEARCH_PARAMS_CONSTANT).getTextContent();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name",headerElementName);
                jsonObject.put("title",title);
                jsonObject.put("enableForRoles",userRoles);
                jsonObject.put("searchParams", searchParams);

                if(containsAny(authentication,userRoles.split(","))){
                    menuElementsList.put(jsonObject);
                }
            }
            return menuElementsList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }


    public JSONArray parseHeaderMenuButtons(Authentication authentication){
        try {
            JSONArray headerElementsList = new JSONArray();
            InputStream inputStream = this.getClass().getResourceAsStream(pathsConfigProperties.getProperty(HEADER_ELEMENT_CONSTANT));
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName(HEADER_ELEMENT_CONSTANT);
            for (int i = 0; i < nodeList.getLength(); i++){
                Node headerElementNode = nodeList.item(i);
                String headerElementName = headerElementNode.getAttributes().getNamedItem(NAME_CONSTANT).getTextContent();
                String title = headerElementNode.getAttributes().getNamedItem(TITLE_CONSTANT).getTextContent();
                String userRoles = headerElementNode.getAttributes().getNamedItem(ENABLE_FOR_USER_ROLES_CONSTANT).getTextContent();
//                String searchParams = headerElementNode.getAttributes().getNamedItem(SEARCH_PARAMS_CONSTANT).getTextContent();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name",headerElementName);
                jsonObject.put("title",title);
                jsonObject.put("enableForRoles",userRoles);
//                jsonObject.put("searchParams", searchParams);

                if(containsAny(authentication,userRoles.split(","))){
                    headerElementsList.put(jsonObject);
                }
            }
            return headerElementsList;

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

}

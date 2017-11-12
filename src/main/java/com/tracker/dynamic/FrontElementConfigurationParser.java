package com.tracker.dynamic;

import com.tracker.dynamic.header.HeaderElements;
import com.tracker.dynamic.menu.MenuElements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class FrontElementConfigurationParser {

    @Autowired
    private Properties mailProperties;
    private static final String EMPTY_ATTRIBUTE_CONSTANT = "#text";
    private static final String ELEMENTS_CONSTANT = "elements";
    private static final String HEADER_ELEMENT_CONSTANT = "header-element";
    private static final String MENU_ELEMENT_CONSTANT = "menu-element";
    private static final String ENABLE_FOR_USER_ROLES_CONSTANT = "enableForRoles";
    private static final String SEARCH_PARAMS_CONSTANT = "searchParams";
    private static final String TITLE_CONSTANT = "title";
    private static final String NAME_CONSTANT = "name";



    public List<MenuElements> parseMenuButtons(Authentication authentication){
        try {
            List<MenuElements> menuElementsList = new ArrayList<MenuElements>();
            InputStream inputStream = this.getClass().getResourceAsStream(mailProperties.getProperty(MENU_ELEMENT_CONSTANT));
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

                if(containsAny(authentication,userRoles.split(","))){
                    menuElementsList.add(new MenuElements(headerElementName,title,searchParams,userRoles));
                }
            }
            return menuElementsList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<MenuElements>();
    }


    public List<HeaderElements> parseHeaderMenuButtons(Authentication authentication){
        try {
            List<HeaderElements> headerElementsList = new ArrayList<HeaderElements>();
            InputStream inputStream = this.getClass().getResourceAsStream(mailProperties.getProperty(HEADER_ELEMENT_CONSTANT));
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName(HEADER_ELEMENT_CONSTANT);
            for (int i = 0; i < nodeList.getLength(); i++){
                Node headerElementNode = nodeList.item(i);
                String headerElementName = headerElementNode.getAttributes().getNamedItem(NAME_CONSTANT).getTextContent();
                String title = headerElementNode.getAttributes().getNamedItem(TITLE_CONSTANT).getTextContent();
                String userRoles = headerElementNode.getAttributes().getNamedItem(ENABLE_FOR_USER_ROLES_CONSTANT).getTextContent();

                if(containsAny(authentication,userRoles.split(","))){
                    headerElementsList.add(new HeaderElements(headerElementName,title,userRoles));
                }
            }
            return headerElementsList;

        } catch (Exception e) {
            e.printStackTrace();
        }
       return new ArrayList<HeaderElements>();
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

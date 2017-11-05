package com.tracker.dynamic;

import com.tracker.dynamic.header.HeaderElements;
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
    private static final String HEADER_ELEMENTS_CONSTANT = "header-elements";
    private static final String HEADER_ELEMENT_CONSTANT = "header-element";
    private static final String ENABLE_FOR_USER_ROLES_CONSTANT = "enableForRoles";
    private static final String TITLE_CONSTANT = "title";
    private static final String NAME_CONSTANT = "name";

    public void parse(String filePath){
        try {
            List<HeaderElements> headerElementsList = new ArrayList<HeaderElements>();
            InputStream inputStream = this.getClass().getResourceAsStream(mailProperties.getProperty(HEADER_ELEMENTS_CONSTANT));
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName(HEADER_ELEMENT_CONSTANT);
            for (int i = 0; i < nodeList.getLength(); i++){
                Node headerElementNode = nodeList.item(i);
                String headerElementName = headerElementNode.getNodeName();
                System.out.println("**************");
                System.out.println(headerElementName);

                NodeList headerElementAttributes = headerElementNode.getChildNodes();
                for (int j = 0; j < headerElementAttributes.getLength(); j++){
                    Node headerElementAttributeNode = headerElementAttributes.item(j);
                    String attributeName = headerElementAttributeNode.getNodeName();
                    String attributeValue = headerElementAttributeNode.getTextContent();
                    if(!attributeName.endsWith(EMPTY_ATTRIBUTE_CONSTANT)){
                        System.out.println("--------------------");
                        System.out.println(attributeName);
                        System.out.println(attributeValue);
//                        headerElementsList.add(new HeaderElements(headerElementName))
                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<HeaderElements> parseConfigurationFiles(Authentication authentication){
        try {
            List<HeaderElements> headerElementsList = new ArrayList<HeaderElements>();
            InputStream inputStream = this.getClass().getResourceAsStream(mailProperties.getProperty(HEADER_ELEMENTS_CONSTANT));
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName(HEADER_ELEMENT_CONSTANT);
            for (int i = 0; i < nodeList.getLength(); i++){
                Node headerElementNode = nodeList.item(i);
                String headerElementName = headerElementNode.getAttributes().getNamedItem(NAME_CONSTANT).getTextContent();
                String title = headerElementNode.getAttributes().getNamedItem(TITLE_CONSTANT).getTextContent();
                String userRoles = headerElementNode.getAttributes().getNamedItem(ENABLE_FOR_USER_ROLES_CONSTANT).getTextContent();

//                boolean vehiclesContainDodge = authentication.getAuthorities().
                if(containsAny(authentication,userRoles.split(","))){
                    headerElementsList.add(new HeaderElements(headerElementName,title,userRoles));
                    System.out.println("**************");
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

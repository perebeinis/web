package com.tracker.cards.user;

import com.tracker.config.SpringWebConfig;
import com.tracker.dynamic.menu.MenuElements;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.core.Authentication;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserCard {

    private Properties pathsConfigProperties;

    private JSONArray userData = new JSONArray();

    private static final String USER_CARD = "user-card";
    private static final String PROPERTY_CONSTANT = "property";
    private static final String TITLE_CONSTANT = "title";
    private static final String NAME_CONSTANT = "name";
    private static final String TYPE_CONSTANT = "type";


    public UserCard() {
    }

    public void createUserData() {
        try {
            JSONArray attributeList = new JSONArray();
            InputStream inputStream = this.getClass().getResourceAsStream(pathsConfigProperties.getProperty(USER_CARD));
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName(PROPERTY_CONSTANT);
            for (int i = 0; i < nodeList.getLength(); i++){
                Node headerElementNode = nodeList.item(i);
                String elementName = headerElementNode.getAttributes().getNamedItem(NAME_CONSTANT).getTextContent();
                String title = headerElementNode.getAttributes().getNamedItem(TITLE_CONSTANT).getTextContent();
                String type = headerElementNode.getAttributes().getNamedItem(TYPE_CONSTANT).getTextContent();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(NAME_CONSTANT,elementName);
                jsonObject.put(TITLE_CONSTANT,title);
                jsonObject.put(TYPE_CONSTANT,type);
                attributeList.put(jsonObject);

            }
            userData =  attributeList;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONArray getUserData() {
        return userData;
    }

    public void setPathsConfigProperties(PropertiesFactoryBean pathsConfigProperties) {
        try {
            this.pathsConfigProperties = pathsConfigProperties.getObject();
        }catch (Exception e){
            System.out.println("eee");
        }

    }
}

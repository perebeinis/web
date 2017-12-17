package com.tracker.cards;

import com.mongodb.client.MongoDatabase;
import com.tracker.dynamic.FrontElementConfigurationParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Properties;

public class CardDataProcessor {

    protected MessageSource messageSource;

    protected FrontElementConfigurationParser frontElementConfigurationParser;

    protected MongoDatabase database;

    protected Properties pathsConfigProperties;


    private static final String USER_CARD = "user-card";
    private static final String PROPERTY_CONSTANT = "property";
    private static final String SET_CONSTANT = "set";
    private static final String TITLE_CONSTANT = "title";
    private static final String NAME_CONSTANT = "name";
    private static final String TYPE_CONSTANT = "type";
    private static final String BUTTONS_CONSTANT = "buttons";
    private static final String CUSTOM_CLASS_NAME_CONSTANT = "customClassName";
    private static final String MANDATORY_CONDITIONS_CONSTANT = "mandatoryCondition";

    public CardDataProcessor() {
        System.out.println("aaa");
    }

    public JSONArray parseData(Document document, String parentTagName){
        JSONArray setList = new JSONArray();
        NodeList nodeList = document.getElementsByTagName(parentTagName);
        for (int i = 0; i < nodeList.getLength(); i++){
            Node setElementNode = nodeList.item(i);
            String setName = setElementNode.getAttributes().getNamedItem(NAME_CONSTANT).getTextContent();
            String setTitle = setElementNode.getAttributes().getNamedItem(TITLE_CONSTANT).getTextContent();
            JSONObject jsonSetObject = new JSONObject();
            jsonSetObject.put(NAME_CONSTANT, setName );
            jsonSetObject.put(TITLE_CONSTANT, setTitle);



            NodeList nodeAttributesList = setElementNode.getChildNodes();
            JSONArray attributeList = new JSONArray();
            for (int j = 0; j < nodeAttributesList.getLength(); j++){
                Node attributeElementNode = nodeAttributesList.item(j);
                if(attributeElementNode.getAttributes()!=null){
                    String elementName = attributeElementNode.getAttributes().getNamedItem(NAME_CONSTANT).getTextContent();
                    String title = attributeElementNode.getAttributes().getNamedItem(TITLE_CONSTANT).getTextContent();
                    String type = attributeElementNode.getAttributes().getNamedItem(TYPE_CONSTANT).getTextContent();
                    String className = attributeElementNode.getAttributes().getNamedItem(CUSTOM_CLASS_NAME_CONSTANT).getTextContent();
                    String mandatoryCondition = "";
                    if(attributeElementNode.getAttributes().getNamedItem(MANDATORY_CONDITIONS_CONSTANT)!=null){
                        mandatoryCondition = attributeElementNode.getAttributes().getNamedItem(MANDATORY_CONDITIONS_CONSTANT).getTextContent();
                    }

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(NAME_CONSTANT,elementName);
                    jsonObject.put(TITLE_CONSTANT,title);
                    jsonObject.put(TYPE_CONSTANT,type);
                    jsonObject.put(CUSTOM_CLASS_NAME_CONSTANT,className);
                    jsonObject.put(MANDATORY_CONDITIONS_CONSTANT,mandatoryCondition);
                    attributeList.put(jsonObject);
                }
            }

            jsonSetObject.put(SET_CONSTANT, attributeList);
            setList.put(jsonSetObject);
        }

        return setList;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setFrontElementConfigurationParser(FrontElementConfigurationParser frontElementConfigurationParser) {
        this.frontElementConfigurationParser = frontElementConfigurationParser;
    }

    public void setDatabase(MongoDatabase database) {
        this.database = database;
    }

    public void setPathsConfigProperties(Properties pathsConfigProperties) {
        this.pathsConfigProperties = pathsConfigProperties;
    }
}

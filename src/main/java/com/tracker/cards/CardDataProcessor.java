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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class CardDataProcessor {

    @Autowired
    protected MessageSource messageSource;

    @Autowired
    protected FrontElementConfigurationParser frontElementConfigurationParser;

    @Autowired
    protected MongoDatabase database;

    private Properties pathsConfigProperties;

    private static final String SET_CONSTANT = "set";
    private static final String TITLE_CONSTANT = "title";
    private static final String NAME_CONSTANT = "name";
    private static final String TYPE_CONSTANT = "type";
    private static final String BUTTONS_CONSTANT = "buttons";
    private static final String CUSTOM_CLASS_NAME_CONSTANT = "customClassName";
    private static final String TYPE_FOR_SAVING = "typeForSaving";
    private static final String MANDATORY_CONDITIONS_CONSTANT = "mandatoryCondition";
    private static Map<String,JSONArray> buttonsForCard = new HashMap<>();


    public JSONArray  parseData(Document document, String parentTagName){
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
                    String typeForSaving = attributeElementNode.getAttributes().getNamedItem(TYPE_FOR_SAVING)!=null ? attributeElementNode.getAttributes().getNamedItem(TYPE_FOR_SAVING).getTextContent() : "text";
                    String className = attributeElementNode.getAttributes().getNamedItem(CUSTOM_CLASS_NAME_CONSTANT).getTextContent();
                    String mandatoryCondition = "";
                    if(attributeElementNode.getAttributes().getNamedItem(MANDATORY_CONDITIONS_CONSTANT)!=null){
                        mandatoryCondition = attributeElementNode.getAttributes().getNamedItem(MANDATORY_CONDITIONS_CONSTANT).getTextContent();
                    }

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(NAME_CONSTANT,elementName);
                    jsonObject.put(TITLE_CONSTANT,title);
                    jsonObject.put(TYPE_CONSTANT,type);
                    jsonObject.put(TYPE_FOR_SAVING, typeForSaving);
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


    public  JSONObject createCardData(String elementType) {
        JSONObject jsonObject = new JSONObject();
        try {
            InputStream inputStream = this.getClass().getResourceAsStream(pathsConfigProperties.getProperty(elementType));
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            document.getDocumentElement().normalize();

            JSONArray setList = parseData(document, SET_CONSTANT);
            JSONArray buttons = parseData(document, BUTTONS_CONSTANT);

            jsonObject.put(SET_CONSTANT, setList);
            jsonObject.put(BUTTONS_CONSTANT, buttons);
            return jsonObject;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }


    public MessageSource getMessageSource() {
        return messageSource;
    }

    public FrontElementConfigurationParser getFrontElementConfigurationParser() {
        return frontElementConfigurationParser;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public Properties getPathsConfigProperties() {
        return pathsConfigProperties;
    }

    public void setPathsConfigProperties(Properties pathsConfigProperties) {
        try {
            this.pathsConfigProperties = pathsConfigProperties;
        }catch (Exception e){
            System.out.println("Error "+e);
        }

    }

    public static Map<String, JSONArray> getButtonsForCard() {
        return buttonsForCard;
    }
}

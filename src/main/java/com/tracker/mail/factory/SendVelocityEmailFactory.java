package com.tracker.mail.factory;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.tracker.constants.BaseConstants;
import com.tracker.mail.send.impl.GmailSender;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.ui.ModelMap;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * Created by Perebeinis on 05.05.2018.
 */
public class SendVelocityEmailFactory {

    private Properties pathsConfigProperties;
    private JSONObject configurationData;

    @Autowired
    private VelocityEngine velocityEngine;

    private static final String MAIL_TEMPLATE_CONFIG_FILE = "mail-template-config-file";

    public SendVelocityEmailFactory(PropertiesFactoryBean pathsConfigProperties) throws IOException {
        this.pathsConfigProperties = pathsConfigProperties.getObject();
    }

    public boolean sendVelocityEmail(String template, String recipient, ModelMap modelMap) {
        JSONObject configData = getConfigData();
        JSONObject templateData = configData.getJSONObject(template);
        String messageText = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateData.getString(BaseConstants.PATH), BaseConstants.DEFAULT_ENCODING, modelMap);
        boolean messageSent = new GmailSender().sendMail(templateData.getString(BaseConstants.THEME), messageText, recipient);
        return messageSent;
    }

    public synchronized JSONObject getConfigData() {
        if (configurationData == null) {
            String configFilePath = pathsConfigProperties.getProperty(MAIL_TEMPLATE_CONFIG_FILE);
            URL fileUrl = getClass().getResource(configFilePath);
            File file = new File(fileUrl.getFile());

            try (Reader reader = new InputStreamReader(new FileInputStream(file));) {
                JsonParser parser = new JsonParser();
                JsonElement jsonElement = parser.parse(reader);
                configurationData = new JSONObject(jsonElement.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return configurationData;
    }
}

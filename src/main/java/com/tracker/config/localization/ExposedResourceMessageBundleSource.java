package com.tracker.config.localization;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Logger;

public class ExposedResourceMessageBundleSource extends ReloadableResourceBundleMessageSource {

    @Override
    protected Properties loadProperties(Resource resource, String fileName) throws IOException {
//        LOGGER.info("Load " + fileName);
        return super.loadProperties(resource, fileName);
    }

    public Properties getMessages(Locale locale){
        return getMergedProperties(locale).getProperties();
    }
}

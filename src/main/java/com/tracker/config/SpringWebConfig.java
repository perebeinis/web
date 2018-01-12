/*
 * =============================================================================
 *
 *   Copyright (c) 2011-2016, The THYMELEAF team (http://www.thymeleaf.org)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 * =============================================================================
 */
package com.tracker.config;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.tracker.cards.CardDataFactory;
import com.tracker.cards.CardDataProcessor;
import com.tracker.config.localization.ExposedResourceMessageBundleSource;
import com.tracker.config.localization.MessageResolveService;
import com.tracker.config.localization.MessageResolverServiceImpl;
import com.tracker.config.security.authentification.impl.UserDetailsServiceImpl;
import com.tracker.controller.base.BaseControllerResponce;
import com.tracker.dao.create.DataCreatorFactory;
import com.tracker.dao.search.DataSearchFactory;
import com.tracker.dao.search.GetElementFactory;
import com.tracker.dynamic.FrontElementConfigurationParser;
import com.tracker.news.impl.NewsObserver;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

@Configuration
@EnableWebMvc
@ComponentScan({ "com.tracker"})
public class SpringWebConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware {


    private ApplicationContext applicationContext;
    @Autowired
    private DatabaseMessageSource databaseMessageSource;



    public SpringWebConfig() {
        super();
    }


    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    public ExposedResourceMessageBundleSource messageSource() {
        ExposedResourceMessageBundleSource messageSource = new ExposedResourceMessageBundleSource();
        messageSource.setBasename("classpath:/messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(10);
        return messageSource;
    }

    @Bean
    public CookieLocaleResolver localeResolver() {
        CookieLocaleResolver slr = new CookieLocaleResolver();
        slr.setDefaultLocale(new Locale("uk","UA"));
        return slr;
    }


    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);
    }


    @Bean
    public SpringResourceTemplateResolver templateResolver(){
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(this.applicationContext);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        // Template cache is true by default. Set to false if you want
        // templates to be automatically updated when modified.
        templateResolver.setCacheable(true);
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine(){
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setEnableSpringELCompiler(true); // Compiled SpringEL should speed up executions
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.addDialect(new SpringSecurityDialect());
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver(){
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }


    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("/images/**").addResourceLocations("/images/");
        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("/js/**").addResourceLocations("/js/");
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }

    @Bean
    public PropertiesFactoryBean pathsConfigProperties() throws IOException {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocation(new ClassPathResource("com/tracker/config/properties/paths.properties"));
        return bean;
    }

    @Bean
    public FrontElementConfigurationParser frontElementConfigurationParser(PropertiesFactoryBean pathsConfigProperties) throws IOException {
        FrontElementConfigurationParser bean = new FrontElementConfigurationParser();
        bean.setPathsConfigProperties(pathsConfigProperties);
        return bean;
    }

    @Bean
    public MongoDatabase database(){
        MongoClient mongo = new MongoClient( "localhost" , 27017 );
        MongoDatabase database = mongo.getDatabase("tracker");
        return database;
    }


    @Bean
    public MessageResolveService messageResolveService(MessageSource messageSource){
        MessageResolverServiceImpl messageResolveService = new MessageResolverServiceImpl();
        messageResolveService.setMessageSource(messageSource);
        return messageResolveService;
    }

    @Bean
    public UserDetailsService customUserDetailsService(MongoDatabase database){
        return new UserDetailsServiceImpl(database);
    }

    @Bean
    public BaseControllerResponce baseControllerResponce(){
        return new BaseControllerResponce();
    }


    @Bean
    public SpringTemplateEngine thymeleafTemplateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        // ...
        engine.setTemplateEngineMessageSource(databaseMessageSource);
        return engine;
    }


    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public CardDataProcessor cardDataProcessor(){
        return new CardDataProcessor().getInstance();
    }

    @Bean
    public CardDataFactory cardDataFactory(){
        return new CardDataFactory();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties(){
        PropertySourcesPlaceholderConfigurer pspc =
                new PropertySourcesPlaceholderConfigurer();
        Resource[] resources = new ClassPathResource[ ]
                { new ClassPathResource( "com/tracker/config/properties/paths.properties" ) };
        pspc.setLocations( resources );
        pspc.setIgnoreUnresolvablePlaceholders( true );
        return pspc;
    }


    @Bean
    public DataSearchFactory dataSearchFactory(){
        return new DataSearchFactory();
    }

    @Bean
    public DataCreatorFactory dataCreatorFactory(){
        return new DataCreatorFactory();
    }

    @Bean
    public GetElementFactory getElementFactory(){
        return new GetElementFactory();
    }

    @Bean
    public NewsObserver newsObserver(MongoDatabase database){
        NewsObserver newsObserver = new NewsObserver();
        newsObserver.setDatabase(database);
        newsObserver.init();
        return newsObserver;
    }

}

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

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.tracker.config.localization.ExposedResourceMessageBundleSource;
import com.tracker.config.localization.MessageResolveService;
import com.tracker.config.localization.MessageResolverServiceImpl;
import com.tracker.config.security.authentification.impl.UserDetailsServiceImpl;
import com.tracker.constants.BaseConstants;
import com.tracker.controller.base.BaseControllerResponse;
import com.tracker.dao.process.audit.AuditService;
import com.tracker.dao.process.data.DataProcessorFactory;
import com.tracker.dao.process.data.DataProcessorService;
import com.tracker.dao.search.execute.DataSearchFactory;
import com.tracker.observer.ChangingDataObserver;
import com.tracker.observer.Observer;
import com.tracker.observer.impl.CreateHistoryDataSubscriber;
import com.tracker.observer.impl.CreateNewsDataSubscriber;
import com.tracker.observer.impl.TaskProcessDataSubscriber;
import com.tracker.view.elements.ViewElementsDataFactory;
import com.tracker.view.elements.impl.DefaultViewElementsData;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Configuration
@EnableWebMvc
@ComponentScan({"com.tracker"})
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
        messageSource.setDefaultEncoding(BaseConstants.DEFAULT_ENCODING);
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
        templateResolver.setCharacterEncoding(BaseConstants.DEFAULT_ENCODING);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(true);
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine(){
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setEnableSpringELCompiler(true);
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.addDialect(new SpringSecurityDialect());
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver(){
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding(BaseConstants.DEFAULT_ENCODING);
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
        bean.setLocation(new ClassPathResource("com/tracker/config/properties/config.properties"));
        return bean;
    }


    @Bean
    public MongoDatabase database(){
        MongoClient mongo = new MongoClient("localhost" , 27017 );
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
    public BaseControllerResponse baseControllerResponse(UserDetailsService customUserDetailsService){
        return new BaseControllerResponse();
    }


    @Bean
    public SpringTemplateEngine thymeleafTemplateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateEngineMessageSource(databaseMessageSource);
        return engine;
    }


    @Bean(initMethod = "getInstance")
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public DefaultViewElementsData defaultViewElementsData(PropertiesFactoryBean pathsConfigProperties) throws IOException {
        DefaultViewElementsData defaultViewElementsData = new DefaultViewElementsData().getInstance();
        defaultViewElementsData.setPathsConfigProperties(pathsConfigProperties);
        return defaultViewElementsData;
    }

    @Bean
    public ViewElementsDataFactory viewElementsDataFactory(){
        return new ViewElementsDataFactory();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties(){
        PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        Resource[] resources = new ClassPathResource[ ]{new ClassPathResource("com/tracker/config/properties/config.properties" )};
        pspc.setLocations( resources );
        pspc.setIgnoreUnresolvablePlaceholders( true );
        return pspc;
    }


    @Bean
    public DataSearchFactory dataSearchFactory(){
        return new DataSearchFactory();
    }

    @Bean
    public CreateHistoryDataSubscriber createHistoryDataSubscriber(MongoDatabase database){
        CreateHistoryDataSubscriber createHistoryDataSubscriber = new CreateHistoryDataSubscriber();
        createHistoryDataSubscriber.setDatabase(database);
        return new CreateHistoryDataSubscriber();
    }

    @Bean
    public TaskProcessDataSubscriber taskProcessDataSubscriber(){
        return new TaskProcessDataSubscriber();
    }

    @Bean
    public CreateNewsDataSubscriber createNewsDataSubscriber(){
        return new CreateNewsDataSubscriber();
    }



    @Bean
    public ChangingDataObserver changingDataObserver(MongoDatabase database, CreateHistoryDataSubscriber createHistoryDataSubscriber, TaskProcessDataSubscriber taskProcessDataSubscriber, CreateNewsDataSubscriber createNewsDataSubscriber ){
        ChangingDataObserver changingDataObserver = new ChangingDataObserver();
        List<Observer> observers = new ArrayList<>();
        observers.add(createHistoryDataSubscriber);
        observers.add(taskProcessDataSubscriber);
        observers.add(createNewsDataSubscriber);
        changingDataObserver.setDatabase(database);
        changingDataObserver.init(observers);
        return changingDataObserver;
    }

    @Bean
    @DependsOn("defaultViewElementsData")
    public UserDetailsService customUserDetailsService(MongoDatabase database, DefaultViewElementsData defaultViewElementsData){
        return new UserDetailsServiceImpl(database);
    }

    @Bean(initMethod = "getInstance")
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public DataProcessorService dataProcessorService() throws IOException {
        DataProcessorService dataProcessorService = new DataProcessorService().getInstance();
        return dataProcessorService;
    }

    @Bean(initMethod = "getInstance")
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public AuditService auditService() throws IOException {
        return new AuditService().getInstance();
    }

    @Bean
    public DataProcessorFactory dataProcessorFactory(){
        return new DataProcessorFactory();
    }

}

package com.tracker.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

public class ApplicationInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext container) throws ServletException {

		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(ApplicationConfiguration.class);

		container.addListener(new ContextLoaderListener(rootContext));
		container.addFilter("securityFilter", new DelegatingFilterProxy("springSecurityFilterChain")).addMappingForUrlPatterns(null, false, "/*");
		container.addListener(new SessionListener());

		AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();

		ServletRegistration.Dynamic dispatcher = container.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
	}

}

package com.example.blog;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class AppInitializer implements WebApplicationInitializer {

	private static final String CONFIG_LOCATION = "com.example.blog.config";
	private static final String MAPPING_URL = "/";
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		WebApplicationContext webContext = getWebContext();
		servletContext.addListener(new ContextLoaderListener(webContext));

		ServletRegistration.Dynamic dispatcher =
                servletContext.addServlet("dispatcherServlet", new DispatcherServlet(webContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping(MAPPING_URL);

		// Hidden Http Method Filter
		FilterRegistration.Dynamic hiddenHttpMethodFilter
                = servletContext.addFilter("hiddenHttpMethodFilter", HiddenHttpMethodFilter.class);
		hiddenHttpMethodFilter.addMappingForUrlPatterns(null, false, "/*");
	}
	
	private AnnotationConfigWebApplicationContext getWebContext() {
		AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
		webContext.setConfigLocation(CONFIG_LOCATION);
		return webContext;
	}

}

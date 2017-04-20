package com.example.blog;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;

public class AppInitializer implements WebApplicationInitializer {

	private static final String CONFIG_LOCATION = "com.example.blog.config";
	private static final String MAPPING_URL = "/";

	private static final int MAX_UPLOAD_SIZE = 10 * 1024 * 1024;	// 10MB

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		WebApplicationContext webContext = getWebContext();
		servletContext.addListener(new ContextLoaderListener(webContext));

		ServletRegistration.Dynamic dispatcher =
                servletContext.addServlet("dispatcherServlet", new DispatcherServlet(webContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping(MAPPING_URL);

		// Multipart Config Element
		MultipartConfigElement multipartConfigElement
				= new MultipartConfigElement(System.getProperty("java.io.tmpdir"),
						MAX_UPLOAD_SIZE, MAX_UPLOAD_SIZE * 2, MAX_UPLOAD_SIZE / 2);
		dispatcher.setMultipartConfig(multipartConfigElement);

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

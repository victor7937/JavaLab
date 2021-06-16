package com.epam.esm.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;


public class SpringDispatcher extends AbstractAnnotationConfigDispatcherServletInitializer {


    private static final String DEV_PROFILE = "dev";
    private static final String PROD_PROFILE = "prod";
    private static final String PROFILES_ACTIVE = "spring.profiles.active";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        servletContext.setInitParameter(
                PROFILES_ACTIVE, DEV_PROFILE);
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}


package com.liyu.server.config;

import com.liyu.server.filters.TenantIdCheckFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class Filters {

    @Bean
    public FilterRegistrationBean tenantIdCheckFilterRegistrationBean() {
        System.out.println("Setting up loginRegistrationBean");
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new TenantIdCheckFilter());
        filterRegistrationBean.setUrlPatterns(Collections.singletonList("/api/*"));
        return filterRegistrationBean;
    }
}

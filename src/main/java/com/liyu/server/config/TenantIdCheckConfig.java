package com.liyu.server.config;

import com.liyu.server.filters.TenantIdCheckAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class TenantIdCheckConfig extends WebMvcConfigurerAdapter {
    private TenantIdCheckAdapter tenantIdCheckAdapter() {
        return new TenantIdCheckAdapter();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tenantIdCheckAdapter())
                // 设置通用的 URL 过滤规则
                .addPathPatterns("/api/**")
                // 排除一些 URL
                .excludePathPatterns("/api/login", "/api/tenants/current");
        super.addInterceptors(registry);
    }
}

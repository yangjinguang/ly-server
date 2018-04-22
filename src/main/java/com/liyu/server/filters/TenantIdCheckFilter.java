package com.liyu.server.filters;


import com.liyu.server.utils.APIResponse;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class TenantIdCheckFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String tenantId = ((HttpServletRequest) request).getHeader("X-TENANT-ID");
        log.info("tenant id: " + tenantId);
        if (tenantId == null) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(400);
            APIResponse apiResponse = APIResponse.failed("X-TENANT-ID can not be empty!!");
            httpServletResponse.getWriter().write(apiResponse.toJSON());
            httpServletResponse.getWriter().close();
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}

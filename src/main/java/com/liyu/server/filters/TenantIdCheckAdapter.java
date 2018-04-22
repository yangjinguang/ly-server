package com.liyu.server.filters;

import com.liyu.server.enums.APIResponseCodeEnum;
import com.liyu.server.utils.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class TenantIdCheckAdapter extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        String tenantId = httpServletRequest.getHeader("X-TENANT-ID");
        log.info("tenant id: " + tenantId);
        if (tenantId == null) {
            httpServletResponse.setStatus(403);
            APIResponse apiResponse = APIResponse.failed(APIResponseCodeEnum.TENANT_ID_CHECK_FAILED, "X-TENANT-ID can not be empty!!");
            httpServletResponse.getWriter().write(apiResponse.toJSON());
            httpServletResponse.getWriter().close();
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView)
            throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e)
            throws Exception {

    }
}

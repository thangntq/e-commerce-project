package com.shopme.site.setting;

import com.shopme.common.entity.Setting;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.List;

@Component
public class SettingFilter implements Filter {
    @Autowired
    private SettingService service;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String url = servletRequest.getRequestURI().toString();

        if (url.endsWith(".css") || url.endsWith(".js") || url.equals(".png")){
            filterChain.doFilter(request,response);
            return;
        }

        List<Setting> generalSettings = service.getGeneralSettings();

        generalSettings.forEach(setting -> {
            System.out.println(setting);
            request.setAttribute(setting.getKey(),setting.getValue());
        });
        System.out.println(url);
        filterChain.doFilter(request,response);
    }
}

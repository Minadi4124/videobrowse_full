package com.nsoft.sliit.videobrowse.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RedirectAfterLogin extends SavedRequestAwareAuthenticationSuccessHandler {
    public RedirectAfterLogin(){
        setDefaultTargetUrl("/");
        setAlwaysUseDefaultTargetUrl(false);
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        String redirect = request.getParameter("redirect");
        if (redirect != null && !redirect.isBlank()) {
            getRedirectStrategy().sendRedirect(request, response, redirect);
            return;
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}



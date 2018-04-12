package com.plug.united.auth.ajax;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plug.united.auth.UserDetailsImpl;
import com.plug.united.auth.jwt.JwtInfo;
import com.plug.united.comm.utils.JwtUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class AjaxSecurityHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
	                                    HttpServletResponse response,
	                                    Authentication authentication)
	                                    		throws IOException, ServletException {
		UserDetails userDetails = new UserDetailsImpl(authentication.getPrincipal().toString(), new ArrayList<>(authentication.getAuthorities()));
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		
		String accessToken = JwtUtil.createAccessToken(userDetails);
		String refreshToken = JwtUtil.createRefreshToken(userDetails);
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Max-Age", "10000000");
		//response.addHeader (JwtInfo.ACCESS_HEADER_NAME, accessToken);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        Map<String, String> tokenMap = new HashMap<String, String>();
        tokenMap.put(JwtInfo.ACCESS_HEADER_NAME, accessToken);
        tokenMap.put(JwtInfo.REFRESH_HEADER_NAME, refreshToken);
        
        objectMapper.writeValue(response.getWriter(), tokenMap);
        
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
	                                    HttpServletResponse response,
	                                    AuthenticationException exception) {
		logger.debug("onAuthenticationFailure");
		throw new ResponseStatusException(HttpStatus.FORBIDDEN, exception.getMessage());
	}
}

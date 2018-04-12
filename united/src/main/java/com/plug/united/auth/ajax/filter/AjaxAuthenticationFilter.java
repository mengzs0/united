package com.plug.united.auth.ajax.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plug.united.account.entity.Account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

public class AjaxAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private final ObjectMapper objectMapper;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public AjaxAuthenticationFilter(RequestMatcher requestMatcher, ObjectMapper objectMapper) {
		super(requestMatcher);
		this.objectMapper = objectMapper;
		logger.debug("attemptAuthentication");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {

		logger.debug("attemptAuthentication");
		if (isJson(request)) {
			Account account = objectMapper.readValue(request.getReader(), Account.class);
			
			logger.debug("acctId:" + account.getAcctId());
			logger.debug("password:" + account.getPassword());
			
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(account.getAcctId(), account.getPassword());
			
			return getAuthenticationManager().authenticate(authentication);
		}else {
			throw new AccessDeniedException("Don't use content type for " + request.getContentType());
		}
	}

	private boolean isJson(HttpServletRequest request) {
		logger.debug(request.getContentType());
		return MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(request.getContentType())
				|| MediaType.APPLICATION_JSON_UTF8_VALUE.equalsIgnoreCase(request.getContentType());
	}
}

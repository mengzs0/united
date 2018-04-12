package com.plug.united.auth.jwt.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.plug.united.auth.jwt.JwtAuthenticationToken;
import com.plug.united.auth.jwt.JwtInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public JwtAuthenticationFilter(RequestMatcher requestMatcher) {
		super(requestMatcher);
		logger.debug("JwtAuthenticationFilter");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
	                                            HttpServletResponse response) throws AuthenticationException {
		
		String token = request.getHeader(JwtInfo.ACCESS_HEADER_NAME);

		logger.debug(request.getMethod());
		
		if(request.getRequestURI().equals("/refreshToken")){
			logger.debug(request.getRequestURI());
			token = request.getHeader(JwtInfo.REFRESH_HEADER_NAME);
		}
		
		if(request.getMethod().equalsIgnoreCase("OPTIONS")){
			logger.debug("Method OPTIONS");
			//response.setHeader("Access-Control-Allow-Origin", "*");
			
			return new JwtAuthenticationToken("","",null);
		}
		
		if (StringUtils.isEmpty(token)) {
			throw new AccessDeniedException("Not empty Token");
		} else {
			return getAuthenticationManager().authenticate(new JwtAuthenticationToken(token));
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
	                                        HttpServletResponse response,
	                                        FilterChain chain,
	                                        Authentication authResult) throws IOException, ServletException {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authResult);
		SecurityContextHolder.setContext(context);
		chain.doFilter(request, response);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
	                                          HttpServletResponse response,
	                                          AuthenticationException failed) throws IOException, ServletException {
		logger.debug("unsuccessfulAuthentication start");
		logger.debug(failed.getLocalizedMessage());
		logger.debug(failed.getMessage());
		logger.debug(failed.toString());
		
		/*
		String token = request.getHeader(JwtInfo.ACCESS_HEADER_NAME);
		
		JWTVerifier verifier = JWT.require(JwtInfo.getAlgorithm()).build();
		DecodedJWT decodedJWT = verifier.verify(token);
		
		logger.debug("ID:" + decodedJWT.getClaim("id").asString());
		logger.debug("role" + decodedJWT.getClaim("role").asString());
		logger.debug("getExpiresAt" + decodedJWT.getExpiresAt().toString());
		*/
		
		
		SecurityContextHolder.clearContext();
		getFailureHandler().onAuthenticationFailure(request, response, failed);
	}
}

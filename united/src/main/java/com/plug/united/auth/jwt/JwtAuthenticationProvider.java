package com.plug.united.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Clock;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.plug.united.comm.utils.JwtUtil;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		logger.debug("JwtAuthenticationProvider:authenticate");
		if (authentication.getCredentials() == null) {
			throw new BadCredentialsException("Bad token");
		}

		String token = authentication.getCredentials().toString();
		

		JWTVerifier verifier = JWT.require(JwtInfo.getAlgorithm()).build();
		
		try{
			verifier.verify(token);

			UserDetails userDetails = userDetailsService.loadUserByUsername(token);
			return new JwtAuthenticationToken(userDetails.getUsername(), token, userDetails.getAuthorities());
		}catch(TokenExpiredException te){
			logger.debug(te.toString());
			logger.debug(te.getMessage());
			throw new BadCredentialsException("EXP");
		}catch(JWTDecodeException de){
			logger.debug(de.toString());
			logger.debug(de.getMessage());
			throw new BadCredentialsException("INV");
		}catch(SignatureVerificationException de){
			logger.debug(de.toString());
			logger.debug(de.getMessage());
			throw new BadCredentialsException("FUK");
		}catch(Exception de){
			logger.debug(de.toString());
			logger.debug(de.getMessage());
			throw new BadCredentialsException("XXX");
		}
			/*
		if (JwtUtil.verify(token)) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(token);
			return new JwtAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
		} else {
			
			throw new BadCredentialsException("exp");
		}
		*/
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return JwtAuthenticationToken.class.isAssignableFrom(authentication);
	}
}

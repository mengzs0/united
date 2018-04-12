package com.plug.united.auth.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.plug.united.auth.UserDetailsImpl;
import com.plug.united.comm.utils.JwtUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JwtUserDetailsService implements UserDetailsService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public UserDetails loadUserByUsername(String token) {
		DecodedJWT decodedJWT = JwtUtil.tokenToJwt(token);

		if (decodedJWT == null) {
			throw new BadCredentialsException("Not used Token");
		}

		logger.debug("ID:" + decodedJWT.getClaim("id").asString());
		logger.debug("role" + decodedJWT.getClaim("role").asString());
		logger.debug("getExpiresAt" + decodedJWT.getExpiresAt().toString());
		
		
		String id = decodedJWT.getClaim("id").asString();
		String role = decodedJWT.getClaim("role").asString();

		return new UserDetailsImpl(id, AuthorityUtils.createAuthorityList(role));
	}
}

package com.plug.united.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plug.united.auth.UserDetailsImpl;
import com.plug.united.auth.jwt.JwtAuthenticationToken;
import com.plug.united.auth.jwt.JwtInfo;
import com.plug.united.auth.jwt.JwtUserDetailsService;
import com.plug.united.comm.utils.JwtUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class RefreshController {

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@Autowired
	private ObjectMapper objectMapper;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping
	@RequestMapping("/accessToken")
	public ResponseEntity<String> refreshAccessToken(Authentication authentication) {
		System.out.println("refreshToken");
		UserDetails userDetails = new UserDetailsImpl(authentication.getPrincipal().toString(), new ArrayList<>(authentication.getAuthorities()));

		String token = JwtUtil.createAccessToken(userDetails);

		HttpHeaders headers = new HttpHeaders();
		headers.add(JwtInfo.ACCESS_HEADER_NAME, token);

		return ResponseEntity.status(HttpStatus.OK).headers(headers).build();
	}
		
	@RequestMapping(value="/refreshToken", method=RequestMethod.POST)
    public void refreshRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		String refreshToken = request.getHeader(JwtInfo.REFRESH_HEADER_NAME);
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(refreshToken);
		
		String accessToken = JwtUtil.createAccessToken(userDetails);
		
        DecodedJWT jwt = JWT.decode(refreshToken);

		logger.debug("ID:" + jwt.getClaim("id").asString());
		logger.debug("role" + jwt.getClaim("role").asString());
		logger.debug("getExpiresAt" + jwt.getExpiresAt().toString());

		if(jwt.getClaim("ttype").asString().equals(JwtInfo.TOKEN_TYPE_REFRESH)){
	        Date today = new Date();
	        if ( (jwt.getExpiresAt().getTime() - today.getTime()) < 1000*60*60*24*8 ) {
	            logger.debug("YAHOOO:" + (jwt.getExpiresAt().getTime() - today.getTime()));
	            //throw new TokenExpiredException(String.format("The Token has expired on %s.", decodedJWT.getExpiresAt()));
	        }
	        refreshToken = JwtUtil.createRefreshToken(userDetails);
		}    	
		
		Map<String, String> tokenMap = new HashMap<String, String>();
        tokenMap.put(JwtInfo.ACCESS_HEADER_NAME, accessToken);
        tokenMap.put(JwtInfo.REFRESH_HEADER_NAME, refreshToken);
        
        objectMapper.writeValue(response.getWriter(), tokenMap);
	}
}

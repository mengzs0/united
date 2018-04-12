package com.plug.united.comm.utils;

import org.springframework.security.core.userdetails.UserDetails;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.plug.united.auth.jwt.JwtInfo;

import java.util.Date;

public class JwtUtil {

	public static String createAccessToken(UserDetails userDetails) {
		return createToken(userDetails, DateUtil.nowAfterDaysToDate(JwtInfo.ACCESS_EXPIRES_LIMIT), JwtInfo.TOKEN_TYPE_ACCESS);
	}

	public static String createRefreshToken(UserDetails userDetails) {
		return createToken(userDetails, DateUtil.nowAfterDaysToDate(JwtInfo.REFRESH_EXPIRES_LIMIT), JwtInfo.TOKEN_TYPE_REFRESH);
	}
	
	private static String createToken(UserDetails userDetails, Date date, String ttype) {
		try {
			return JWT.create()
					.withIssuer(JwtInfo.ISSUER)
					.withClaim("id", userDetails.getUsername())
					.withClaim("ttype", ttype)
					.withClaim("role", userDetails.getAuthorities().toArray()[0].toString())
					.withExpiresAt(date)
					.sign(JwtInfo.getAlgorithm());
		} catch (JWTCreationException createEx) {
			return null;
		}
	}

	public static Boolean verify(String token) {
		try {
			JWTVerifier verifier = JWT.require(JwtInfo.getAlgorithm()).build();
			verifier.verify(token);
			
			return Boolean.TRUE;
		} catch (JWTVerificationException verifyEx) {
			return Boolean.FALSE;
		}
	}
	
	public static DecodedJWT tokenToJwt(String token) {
		try {
			return JWT.decode(token);
		} catch (JWTDecodeException decodeEx) {
			return null;
		}
	}
}

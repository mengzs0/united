package com.plug.united.auth.jwt;

import com.auth0.jwt.algorithms.Algorithm;

import java.io.UnsupportedEncodingException;

public class JwtInfo {
	private static final String TOKEN_KEY = "plug.united";
	
	public static final String ACCESS_HEADER_NAME = "plugAccessToken";
	public static final String REFRESH_HEADER_NAME = "plugRefreshToken";
	public static final String ISSUER = "plug";
	
	public static final long ACCESS_EXPIRES_LIMIT = 1L;
	public static final String TOKEN_TYPE_ACCESS = "AT";
	
	public static final long REFRESH_EXPIRES_LIMIT = 7L;
	public static final String TOKEN_TYPE_REFRESH = "RT";

	public static Algorithm getAlgorithm() {
		try {
			return Algorithm.HMAC256(JwtInfo.TOKEN_KEY);
		} catch (IllegalArgumentException | UnsupportedEncodingException e) {
			return Algorithm.none();
		}
	}
}

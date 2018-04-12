package com.plug.united.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plug.united.auth.jwt.JwtSecurityHandler;
import com.plug.united.auth.ajax.AjaxAuthenticationProvider;
import com.plug.united.auth.ajax.AjaxSecurityHandler;
import com.plug.united.auth.ajax.AjaxUserDetailsService;
import com.plug.united.auth.ajax.filter.AjaxAuthenticationFilter;
import com.plug.united.auth.jwt.JwtAuthenticationProvider;
import com.plug.united.auth.jwt.JwtUserDetailsService;
import com.plug.united.auth.jwt.filter.JwtAuthenticationFilter;
import com.plug.united.auth.jwt.matcher.SkipPathRequestMatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationProvider jwtProvider;

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	private AjaxAuthenticationProvider ajaxProvider;

	@Autowired
	private AjaxUserDetailsService ajaxUserDetailsService;

	@Autowired
	private AjaxSecurityHandler ajaxSecurityHandler;
	
	@Autowired
	private JwtSecurityHandler jwtSecurityHandler;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private static final String LOGIN_ENTRY_POINT = "/login";
	private static final String REGISTER_ENTRY_POINT = "/register";
	private static final String TOKEN_ENTRY_POINT = "/token";
	private static final String ERROR_ENTRY_POINT = "/error";
	private static final String ROOT_ENTRY_POINT = "/**";
	private static final String WEB_ENTRY_POINT = "/web/**";
	
    private  Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void configure(WebSecurity web) {
		logger.debug("WebSecurity");
		web.ignoring().antMatchers("/resources/**");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		logger.debug("AuthenticationManagerBuilder");
		auth.authenticationProvider(ajaxProvider)
				.authenticationProvider(jwtProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		logger.debug("configure");
		http
				.addFilterBefore(ajaxAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(jwtAuthenticationFilter(), FilterSecurityInterceptor.class)
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers(TOKEN_ENTRY_POINT).permitAll()
				.antMatchers(LOGIN_ENTRY_POINT).permitAll()
				.antMatchers(ERROR_ENTRY_POINT).permitAll()
				.antMatchers(REGISTER_ENTRY_POINT).permitAll()
				.antMatchers(WEB_ENTRY_POINT).permitAll()
				.antMatchers(ROOT_ENTRY_POINT).authenticated();
	}

	@Bean
	public AntPathRequestMatcher antPathRequestMatcher() {
		logger.debug("antPathRequestMatcher");
		return new AntPathRequestMatcher(LOGIN_ENTRY_POINT, HttpMethod.POST.name());
	}

	@Bean
	public AjaxAuthenticationFilter ajaxAuthenticationFilter() throws Exception {
		logger.debug("ajaxAuthenticationFilter");
		AjaxAuthenticationFilter filter = new AjaxAuthenticationFilter(antPathRequestMatcher(), objectMapper);
		filter.setAuthenticationManager(authenticationManager());
		filter.setAuthenticationSuccessHandler(ajaxSecurityHandler);
		filter.setAuthenticationFailureHandler(ajaxSecurityHandler);
		return filter;
	}

	@Bean
	public SkipPathRequestMatcher skipPathRequestMatcher() {
		logger.debug("skipPathRequestMatcher");
		return new SkipPathRequestMatcher(Arrays.asList(LOGIN_ENTRY_POINT, TOKEN_ENTRY_POINT, ERROR_ENTRY_POINT, REGISTER_ENTRY_POINT, WEB_ENTRY_POINT));
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
		logger.debug("jwtAuthenticationFilter");
		JwtAuthenticationFilter filter = new JwtAuthenticationFilter(skipPathRequestMatcher());
		filter.setAuthenticationManager(authenticationManager());
		filter.setAuthenticationFailureHandler(jwtSecurityHandler);
		return filter;
	}
	
	@Bean
	public CorsFilter corsFilter() {
		logger.debug("CorsFilter");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("OPTIONS");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("POST");
		source.registerCorsConfiguration("/**",  config);
		return new CorsFilter(source);
	}
	
}
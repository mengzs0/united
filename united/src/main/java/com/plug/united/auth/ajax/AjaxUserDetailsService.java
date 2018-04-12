package com.plug.united.auth.ajax;

import com.plug.united.auth.UserDetailsImpl;
import com.plug.united.account.entity.Account;
import com.plug.united.account.repository.AccountRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class AjaxUserDetailsService implements UserDetailsService {

	@Autowired
	private AccountRepository repository;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public UserDetails loadUserByUsername(String acctId) {
		Account account = repository.findById(acctId).orElse(null);
		
		logger.debug("AjaxUserDetailsService:" + acctId);

		if (account == null) {
			throw new UsernameNotFoundException(acctId + "라는 사용자가 없습니다.");
		}
		
		if (account.getRoles() == null) throw new InsufficientAuthenticationException("User has no roles assigned");
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		account.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRoleId()));
            logger.debug("ROLE:" + role.getRoleId());
        });
        
		return new UserDetailsImpl(account, authorities);
	}
}

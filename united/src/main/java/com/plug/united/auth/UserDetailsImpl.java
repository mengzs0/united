package com.plug.united.auth;

import com.plug.united.account.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class UserDetailsImpl extends User {

	private static final long serialVersionUID = 1L;

	public UserDetailsImpl(String id, List<GrantedAuthority> authorities) {
		super(id, "", authorities);
	}

	public UserDetailsImpl(Account account, List<GrantedAuthority> authorities) {
		super(account.getAcctId(), account.getPassword(), authorities);
	}
}

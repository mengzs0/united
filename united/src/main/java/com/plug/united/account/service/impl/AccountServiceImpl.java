package com.plug.united.account.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.plug.united.account.entity.Account;
import com.plug.united.account.repository.AccountRepository;
import com.plug.united.account.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountRepository accountRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void saveUser(Account account) {
        accountRepository.save(account);
    }

    @Override
	public Account findOneByAcctId(String acctId) {
    	Account account = accountRepository.findById(acctId).orElse(null);
		return account;
	}
    
	@Override
	public Account findAccountByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

}
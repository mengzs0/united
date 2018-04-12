package com.plug.united.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.plug.united.account.entity.Account;
import com.plug.united.account.repository.AccountRepository;
import com.plug.united.account.service.AccountService;
import com.plug.united.member.service.MemberService;

@RestController
public class AccountRestController {

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private MemberService memberService;

	@Autowired
	private PasswordEncoder passwordEncoder;

    private  Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String add(@RequestBody Account account) {

		Account mCheck = accountService.findOneByAcctId(account.getAcctId());
		if(mCheck != null)
			return "ERROR : User Aleady Exist!";
			
		account = new Account(account.getAcctId(), passwordEncoder.encode(account.getPassword()), account.getAcctName(), account.getEmail());
		accountService.saveUser(account);

		return "SAVED!!";
	}

}
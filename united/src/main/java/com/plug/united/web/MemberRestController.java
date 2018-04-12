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
import com.plug.united.member.entity.Member;
import com.plug.united.member.repository.MemberRepository;
import com.plug.united.member.service.MemberService;

@RestController
public class MemberRestController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private PasswordEncoder passwordEncoder;

    private  Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value="/token", method=RequestMethod.POST)
	public String index(@RequestBody Account account) {
		return "helloworld!";
	}
	
	@RequestMapping(value="/test", method=RequestMethod.POST)
	public String test(@RequestBody Account account) {
		return "asdfsaf helloworld!";
	}
	
}
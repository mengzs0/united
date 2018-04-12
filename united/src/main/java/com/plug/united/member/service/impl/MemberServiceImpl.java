package com.plug.united.member.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.plug.united.member.entity.Member;
import com.plug.united.member.repository.MemberRepository;
import com.plug.united.member.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService{

    @Autowired
    private MemberRepository memberRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void saveUser(Member member) {
        memberRepository.save(member);
    }

    @Override
	public Member findOneByAcctId(String acctId) {
    	Member member = memberRepository.findById(acctId).orElse(null);
		return member;
	}
    
	@Override
	public Member findMemberByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

}
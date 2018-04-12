package com.plug.united.member.service;

import com.plug.united.member.entity.Member;

public interface MemberService {
    public Member findMemberByEmail(String email);
    public void saveUser(Member member);

    public Member findOneByAcctId(String acctId);
}
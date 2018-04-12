package com.plug.united.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.plug.united.member.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
}

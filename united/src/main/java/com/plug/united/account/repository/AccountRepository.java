package com.plug.united.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.plug.united.account.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
}

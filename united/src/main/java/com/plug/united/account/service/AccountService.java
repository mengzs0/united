package com.plug.united.account.service;

import com.plug.united.account.entity.Account;

public interface AccountService {
    public Account findAccountByEmail(String email);
    public void saveUser(Account account);

    public Account findOneByAcctId(String acctId);
}
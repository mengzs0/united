package com.plug.united.member.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.plug.united.account.entity.Account;

@Entity
@Table(name="TB_MEMBER")
public class Member {
	
	@Id
    @Column(name="ACCT_ID")
	private String acctId;

    @OneToOne
    @JoinColumn(name = "ACCT_ID")
    private Account account;
	
    @Column(name="EMAIL")
	private String email;

    @Column(name="PHONE")
	private String phone;

    @Column(name="USER_NAME")
	private String userName;

    @Column(name="NICK_NAME")
	private String nickName;

    @Column(name="CRT_DTM")
	private String crtDtm;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getCrtDtm() {
		return crtDtm;
	}

	public void setCrtDtm(String crtDtm) {
		this.crtDtm = crtDtm;
	}

	public String getAcctId() {
		return acctId;
	}

	public void setAcctId(String acctId) {
		this.acctId = acctId;
	}

}


package com.plug.united.account.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ACCOUNT")
public class Account {
	private String acctId;
	private String password;
	private String acctName;
	private String email;

	private Set<Role> roles = new HashSet<Role>();

	public Account(){};
	public Account(String acctId, String password, String acctName, String email) {
		this.acctId = acctId;
		this.password = password;
		this.acctName = acctName;
		this.email = email;
	}
	
	public void addRole(Role role) {
		this.roles.add(role);
	}

	@Id
	@Column(name = "ACCT_ID")
	public String getAcctId() {
		return acctId;
	}

	public void setAcctId(String acctId) {
		this.acctId = acctId;
	}

	public String getAcctName() {
		return acctName;
	}

	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
			name = "TB_ACCT_ROLE",
			joinColumns = @JoinColumn(name = "ACCT_ID"),
			inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
	)
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}
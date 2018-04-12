package com.plug.united.component;

import com.plug.united.account.entity.Account;
import com.plug.united.account.entity.Role;
import com.plug.united.account.repository.AccountRepository;
import com.plug.united.account.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InitComponent implements ApplicationRunner {

	@Autowired
	private AccountRepository aRepository;
	
	@Autowired
	private RoleRepository rRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	@Override
	public void run(ApplicationArguments args) {
		Role role1 = new Role("USER", "USER");
		Role role2 = new Role("ADMIN", "ADMIN");
		rRepository.save(role1);
		rRepository.save(role2);
		
		Account account1 = new Account("jmkwon@plug.com", passwordEncoder.encode("1234"), "jmkwon", "jmkwon@plug.com");
		account1.addRole(role1);
		account1.addRole(role2);
		aRepository.save(account1);
		
		
		Account account2 = new Account("admin@plug.com", passwordEncoder.encode("1234"), "admin", "admin@plug.com");
		account2.addRole(role1);
		aRepository.save(account2);
		
		//role1.addAccount(account1);
		//role1.addAccount(account2);
		//role2.addAccount(account2);
		
		
		//aRepository.save(account1);
		//aRepository.save(account2);
	
	}
}

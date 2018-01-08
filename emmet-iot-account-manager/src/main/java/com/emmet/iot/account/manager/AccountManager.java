package com.emmet.iot.account.manager;

import org.springframework.stereotype.Service;

import com.emmet.iot.account.manager.exception.AccountNotFoundException;
import com.emmet.iot.core.model.AccountLogin;


@Service
public class AccountManager {
	public AccountLogin getAccountLogin(String accountName) throws AccountNotFoundException{
		if(accountName.equals("guojun")){
			AccountLogin al = new AccountLogin();
			al.setName("guojun");
			al.setPassword("passwd");
			return al;
		}
		
        throw new AccountNotFoundException();
		
	}

}

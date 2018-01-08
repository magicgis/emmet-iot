package com.emmet.iot.account.manager.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.emmet.iot.account.manager.AccountManager;
import com.emmet.iot.account.manager.exception.AccountNotFoundException;
import com.emmet.iot.core.model.AccountLogin;
import com.emmet.iot.core.model.AccountSessionData;

@Controller
public class LoginController {

	private static final Log log = LogFactory.getLog(LoginController.class);
	private static final String USER_SESSION_KEY = "userinfo";

	@Autowired
	AccountManager accountManager;

	@RequestMapping(path = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestParam("username") String username, @RequestParam("password") String password,
			HttpServletRequest thishttpServletRequest) {

		try {
			AccountLogin al = accountManager.getAccountLogin(username);
			if (!password.equals(al.getPassword())) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

		} catch (AccountNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		log.debug("User " + username + "has login.");

		HttpSession session = thishttpServletRequest.getSession(false);
		if (session == null) {
			session = thishttpServletRequest.getSession();
		}
		
		AccountSessionData accountSessionData = new AccountSessionData();
		accountSessionData.setName(username);
		session.setAttribute(USER_SESSION_KEY, accountSessionData);

		return new ResponseEntity<>(HttpStatus.OK);

	}

	@RequestMapping(path = "/logout", method = RequestMethod.POST)
	public ResponseEntity<?> logout(@RequestHeader(value = "x-auth-token") String xauthToken,
			HttpServletRequest thishttpServletRequest) {
		
		HttpSession session = thishttpServletRequest.getSession(false);
		session.removeAttribute(USER_SESSION_KEY);

		return null;

	}

}

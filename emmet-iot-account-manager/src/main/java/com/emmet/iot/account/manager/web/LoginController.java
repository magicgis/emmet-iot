package com.emmet.iot.account.manager.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

	@RequestMapping(path = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestParam("username") String username,
			@RequestParam("password") String password, HttpServletRequest thishttpServletRequest) {
		
	
		HttpSession session = thishttpServletRequest.getSession();
		if(session == null){
			
		}
		
		
		System.out.println(username + "/"  + password);
		
		LoginResponse r = new LoginResponse();
		r.session = thishttpServletRequest.getSession(false).getId();
		System.out.println(r.getSession());
			
		
		return ResponseEntity.ok(r);

	}
	
	class LoginResponse{
		String session;

		public String getSession() {
			return session;
		}

		public void setSession(String session) {
			this.session = session;
		}
		
	}

}

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

	@RequestMapping(path = "/login", method = RequestMethod.GET)
	public ResponseEntity<?> login(@RequestParam("username") String username,
			@RequestParam("password") String password, HttpServletRequest thishttpServletRequest) {
		
		System.out.println(thishttpServletRequest.getRemoteAddr());
		HttpSession session = thishttpServletRequest.getSession();
		if(session == null){
			
		}
		System.out.println(thishttpServletRequest.getSession(false).getId());
		
		return ResponseEntity.ok(username  + "/" + password);

	}

}

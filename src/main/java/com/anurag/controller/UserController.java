package com.anurag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.anurag.component.User;
import com.anurag.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class UserController {
	@Autowired
	UserService userService;
	
	@PostMapping("/Register")
	public String register(User user , Model m ) {
		if(userService.save(user)) {
			m.addAttribute("msg" , "User Registered Successfully!");
		}else {
			m.addAttribute("msg", "User Already Registerd!");
		}
		
		return "login-signup";
	}
	
	@PostMapping("/Login")
	public String login(@RequestParam String email, @RequestParam String password , Model m , HttpSession session) {
		User u = userService.login(email, password);
		if(u == null) {
			m.addAttribute("msg", "Invalid Credentials!");
			return "login-signup";
		}else {
			session.setAttribute("user", u);
			return "UserHome";
		}
	}
	
	@GetMapping("/Logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login-signup";
	}
	
	@GetMapping("/UserHome")
	public String userHome(HttpSession session, Model m) {
		if(session.getAttribute("user") == null) {
			m.addAttribute("msg", "Please Login!");
			return "login-signup";
		}else {
			return "UserHome";
		}
	}
	
	@GetMapping("/login-signup")
	public String loginSignup () {
		return "login-signup";
	}
	
	
	
	
	
}

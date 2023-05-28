package com.board.exam.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.board.exam.domain.ResponseDTO;
import com.board.exam.domain.User;
import com.board.exam.service.UserService;

@Controller
public class UserController {

	private final static Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserService userService;
	
	@Autowired
	HttpSession session;
	
	@GetMapping("/auth/loginForm")
	public String loginForm() {
		return "user/loginForm";
	}
	
	@GetMapping("/auth/joinForm")
	public String joinForm() {
		return "user/joinForm";
	}
	
	@ResponseBody
	@PostMapping("/auth/insert")
	public ResponseDTO<String> insert(@RequestBody User user){
		userService.insert(user);
		return new ResponseDTO<String>(HttpStatus.OK.value(), "user insert seccess!");
	}
	
	@ResponseBody
	@PostMapping("/auth/login")
	public ResponseDTO<Integer> loginchk(@RequestBody User user){
		User principal = userService.loginchk(user);
		if(principal != null) {
			session.setAttribute("principal", principal);
		}else {
			return new ResponseDTO<Integer>(HttpStatus.NOT_FOUND.value(), 0);
		}
		return new ResponseDTO<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@ResponseBody
	@GetMapping("/logout")
	public ResponseDTO<String> logout(HttpSession session) {
		session.invalidate();
		return new ResponseDTO<String>(HttpStatus.OK.value(), "logout!");
	}
	
	@GetMapping("/user/userInfo")
		public String userInfo(Model model) {
		User user = (User)session.getAttribute("principal");
		model.addAttribute("dto", userService.login(user.getUsername()));
		return "user/detailForm";
	}
	
	@GetMapping("/admin/managed")
	public String managed(Model model) {
		model.addAttribute("message", "Admin Managed Form");
		return "user/managedForm";
	}
	
	@ResponseBody
	@PutMapping("/user/update")
	public ResponseDTO<String> update(@RequestBody User user) {
		userService.update(user);
		return new ResponseDTO<String>(HttpStatus.OK.value(), "User update Success!");
	}
	
}

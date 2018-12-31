package com.platzi.platziprofesores.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

	@RequestMapping("/")
	@ResponseBody
	public String main() {
		return "Bienvenido a <a href='http://platzi.com'>platzi</a>";
	}
}

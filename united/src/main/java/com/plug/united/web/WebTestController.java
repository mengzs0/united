package com.plug.united.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebTestController {
	@RequestMapping("/web/hello")
	public String index(Model model) {
		model.addAttribute("name", "Spring");
		return "hello";
	}
	

	@RequestMapping("/web/vue")
	public String vueIndex() {
		return "index";
	}
}

package pl.szleperm.examples.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LayoutController {
	@RequestMapping
	public String showLayout(){
		return "/index.html";
	}
}

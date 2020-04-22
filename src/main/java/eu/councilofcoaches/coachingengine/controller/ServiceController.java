package eu.councilofcoaches.coachingengine.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController {
	@RequestMapping(value="/hello", method= RequestMethod.GET)
	public String hello() {
		return "Hello";
	}
}

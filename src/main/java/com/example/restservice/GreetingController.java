package com.example.restservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

	@Autowired
	private GreetingService greetingService;

	@GetMapping("/greeting")
	public List<Greeting> greeting(@RequestParam(value = "name", defaultValue = "World") String name, @RequestParam(value = "", defaultValue = "") List<String> languages) {
		return greetingService.getGreeting(name, languages);
	}
}

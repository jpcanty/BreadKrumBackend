package com.example.restservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restservice.models.Greeting;
import com.example.restservice.services.GreetingService;

@RestController
public class GreetingController {
	@Autowired
	private GreetingService greetingService;

	//the route users use to call endpoint. In Spot it's a handler 
	@GetMapping("/greeting")
	public List<Greeting> greeting(@RequestParam(value = "name", defaultValue = "World") String name, @RequestParam(value = "", defaultValue = "") List<String> languages) {
		//Greeting Service is called with the getGreeting() method
		return greetingService.getGreeting(name, languages);
	}
}

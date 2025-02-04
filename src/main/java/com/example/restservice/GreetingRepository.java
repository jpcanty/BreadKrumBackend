package com.example.restservice;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

@Repository
public class GreetingRepository {

    private static final String template = "Hello from the repository, %s!";
	private final AtomicLong counter = new AtomicLong();

    public Greeting storeGreeting(String name) {
        final Greeting greeting = new Greeting(counter.incrementAndGet(), String.format(template, name));
        //store in database
        return greeting;
    }
    
}

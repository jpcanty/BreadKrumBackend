package com.example.restservice;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

@Repository
public class GreetingRepository {

    private static final String template = "%s - Hello from the repository, %s!";
	private final AtomicLong counter = new AtomicLong();

    public Greeting storeGreeting(String name, List<String> language) {
        final Greeting greeting = new Greeting(counter.incrementAndGet(), String.format(template, name, language));
        //store in database
        return greeting;
    }
    
}

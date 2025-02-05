package com.example.restservice;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

@Repository
public class GreetingRepository {

    private static final String template = "%s - Hello from the repository, %s!";
	private final AtomicLong counter = new AtomicLong();

    // for each language, return a list of greetings
    // Stackoverflow - https://stackoverflow.com/questions/24882927/using-streams-to-convert-a-list-of-objects-into-a-string-obtained-from-the-tostr
    public List<Greeting> storeGreeting(String name, List<String> languages) {
        //store in database
        return languages.stream().map(language -> new Greeting(counter.incrementAndGet(), String.format(template, language, name))).toList();
    }
    
}

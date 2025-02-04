package com.example.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {
    
    @Autowired
    private GreetingRepository greetingRepository;

    public Greeting getGreeting(String name) {
        return greetingRepository.storeGreeting(name);
    }
}

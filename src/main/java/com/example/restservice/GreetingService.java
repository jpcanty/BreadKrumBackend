package com.example.restservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {
    
    @Autowired
    private GreetingRepository greetingRepository;

    public Greeting getGreeting(String name, List<String> language) {
        return greetingRepository.storeGreeting(name, language);
    }
}

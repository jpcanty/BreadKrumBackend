package com.example.restservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restservice.models.Greeting;
import com.example.restservice.repositories.GreetingRepository;

@Service
public class GreetingService {
    
    @Autowired
    private GreetingRepository greetingRepository;

    public List<Greeting> getGreeting(String name, List<String> languages) {
        //Called the Greeting Repo
        return greetingRepository.storeGreeting(name, languages);
    }
}

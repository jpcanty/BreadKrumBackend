package com.example.restservice;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class GreetingRepository {

    private static final String template = "Hello from the repository, %s!";
	private final AtomicLong counter = new AtomicLong();
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public GreetingRepository() {
        this.restClient = RestClient.builder().build();
        this.objectMapper = new ObjectMapper();
    }

    public List<Greeting> storeGreeting(String name, List<String> languages) {
        return languages.stream()
                    .map(language -> getTranslation(name, language))
                    .map(translation -> new Greeting(counter.incrementAndGet(), translation))
                    .toList();
    }

    private String getTranslation(String name, String language) {
        String q = String.format(template, name);

        String translation;
        try {
            TranslationResponse response = getTranslationThrows(q, language);
            if (response.getData().geTranslations().isEmpty()) {
                translation = "No translations to parse";
            }
            translation = response.getData().geTranslations().get(0).getTranslatedText();
        } catch (JsonProcessingException e) {
            translation = "Unable to parse translation";
        }
        return translation;
    }

    private TranslationResponse getTranslationThrows(String q, String language) throws JsonMappingException, JsonProcessingException {
        String response = restClient.post()
          .uri("https://translation.googleapis.com/language/translate/v2")
          .body(Map.of("q", List.of(q), "target", language))
          .header("Authorization", "Bearer ya29.a0AXeO80TU9hKZNosC6KdpigdtH1FZ71F9NatT0vRvjWtAoKNb8FhkkcGGZrmyQCjhK40cJb1wujgKC9_2_RNoBGPaDih8JSVLH9GaJn4SP74E1j5teux6JpRJmHv_3ZatKNKdeDBwXx8S8HPqR917WtohWVeEDfhtNJWe3Z7I3t4XZQaCgYKAX8SARESFQHGX2MiL4RKj55PYs2rFEVtM3O-Aw0181")
          .header("x-goog-user-project", "astute-asset-450607-p5")
          .header("Content-Type", "application/json")
          .header("charset", "utf-8")
          .retrieve()
          .body(String.class);

        return objectMapper.readValue(response, TranslationResponse.class);
    }
    
}

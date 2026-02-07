package com.startup.roaster.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class GroqClient {

    private static final String GROQ_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    private final RestTemplate restTemplate;
    private final String apiKey;

    @Value("${groq.model}")
    private String model;

    @Value("${groq.system-prompt}")
    private String systemPrompt;

    public GroqClient(
            RestTemplate restTemplate,
            @Value("${groq.api.key}") String apiKey
    ) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    public String call(String userPrompt) {

        System.out.println(
                "GROQ KEY PRESENT: " +
                        (apiKey != null && !apiKey.isBlank())
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                ),
                "temperature", 0.6
        );

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<OpenAIResponse> response;

        try {
            response = restTemplate.postForEntity(
                    GROQ_URL,
                    request,
                    OpenAIResponse.class
            );
        } catch (HttpClientErrorException.TooManyRequests e) {
            throw new RuntimeException(
                    "Groq rate limit hit. Slow down or retry."
            );
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new RuntimeException(
                    "Invalid Groq API key."
            );
        }

        return response.getBody()
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }
}

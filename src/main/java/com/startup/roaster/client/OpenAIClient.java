package com.startup.roaster.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class OpenAIClient {

    private static final String OPENAI_URL =
            "https://api.openai.com/v1/chat/completions";

    private final RestTemplate restTemplate;
    private final String apiKey;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.system-prompt}")
    private String systemPrompt;

    public OpenAIClient(RestTemplate restTemplate,
                        @Value("${openai.api.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    public String call(String userPrompt) {

        System.out.println(
                "OPENAI KEY PRESENT: " +
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
                    OPENAI_URL,
                    request,
                    OpenAIResponse.class
            );
        } catch (HttpClientErrorException.TooManyRequests e) {
            throw new RuntimeException(
                    "OpenAI quota exceeded. Enable mock mode or add billing."
            );
        }

        return response.getBody()
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }
}

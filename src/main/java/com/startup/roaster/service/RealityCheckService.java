package com.startup.roaster.service;

import com.startup.roaster.client.GroqClient;
import com.startup.roaster.client.OpenAIClient;
import com.startup.roaster.dto.RealityCheckRequest;
import org.springframework.stereotype.Service;

@Service
public class RealityCheckService {

//    private final OpenAIClient client;
    private final GroqClient client;

    public RealityCheckService(GroqClient client) {
        this.client = client;
    }

    public String generate(RealityCheckRequest req) {

        StringBuilder prompt = new StringBuilder();
        prompt.append("Startup Idea:\n").append(req.getIdea()).append("\n\n");

        if (req.getTargetUsers() != null)
            prompt.append("Target Users: ").append(req.getTargetUsers()).append("\n");

        if (req.getMonetization() != null)
            prompt.append("Monetization: ").append(req.getMonetization()).append("\n");

        if (req.getGeography() != null)
            prompt.append("Geography: ").append(req.getGeography()).append("\n");

        return client.call(prompt.toString());
    }
}



package com.startup.roaster.dto;

import jakarta.validation.constraints.NotBlank;

public class RealityCheckRequest {

    @NotBlank
    private String idea;

    private String targetUsers;

    private String monetization;
    private String geography;

    public String getIdea() {
        return idea;
    }

    public String getTargetUsers() {
        return targetUsers;
    }

    public String getMonetization() {
        return monetization;
    }

    public String getGeography() {
        return geography;
    }
// getters & setters
}

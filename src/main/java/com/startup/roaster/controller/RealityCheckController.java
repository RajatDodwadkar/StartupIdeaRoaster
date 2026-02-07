package com.startup.roaster.controller;

import com.startup.roaster.dto.RealityCheckRequest;
import com.startup.roaster.dto.RealityCheckResponse;
import com.startup.roaster.service.RealityCheckService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RealityCheckController {

    private final RealityCheckService service;

    public RealityCheckController(RealityCheckService service) {
        this.service = service;
    }

    @PostMapping("/reality-check")
    public ResponseEntity<RealityCheckResponse> check(
            @Valid @RequestBody RealityCheckRequest req) {

        String output = service.generate(req);
        return ResponseEntity.ok(new RealityCheckResponse(output));
    }
}

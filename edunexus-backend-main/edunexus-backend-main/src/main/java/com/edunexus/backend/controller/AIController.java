package com.edunexus.backend.controller;

import com.edunexus.backend.dto.AIRequest;
import com.edunexus.backend.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

    @PostMapping("/explain")
    public ResponseEntity<?> explain(@RequestBody AIRequest request) {
        return ResponseEntity.ok(aiService.explain(request.getQuestion(), request.getSubjectId()));
    }
}
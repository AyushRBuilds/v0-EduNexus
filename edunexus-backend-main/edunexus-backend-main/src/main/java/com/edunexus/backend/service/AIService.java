package com.edunexus.backend.service;

import com.edunexus.backend.entity.Material;
import com.edunexus.backend.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AIService {

    private final MaterialRepository materialRepository;
    private final RestTemplate restTemplate;

    @Value("${ai.huggingface.url}")
    private String hfUrl;

    @Value("${ai.huggingface.token:}")
    private String hfToken;

    public Object explain(String question, Long subjectId) {
        List<Material> materials = materialRepository.findBySubjectId(subjectId);

        if (materials.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "No material found in college repository");
            response.put("scholarLink", "https://scholar.google.com/scholar?q=" + URLEncoder.encode(question, StandardCharsets.UTF_8));
            return response;
        }

        String combinedContent = materials.stream()
                .map(m -> m.getContent() != null ? m.getContent() : "")
                .collect(Collectors.joining("\n\n"));

        // Truncate context window
        if (combinedContent.length() > 5000) {
            combinedContent = combinedContent.substring(0, 5000);
        }

        String prompt = "You are an academic assistant.\nAnswer the question strictly using the provided content.\n\nContent:\n" +
                combinedContent + "\n\nQuestion:\n" + question;

        return callHuggingFace(prompt);
    }

    private Object callHuggingFace(String prompt) {
        if (hfToken == null || hfToken.isEmpty() || hfToken.equals("YOUR_HF_TOKEN")) {
            return Map.of("answer", "[MOCK AI] This is a simulated response because no API token was provided. " +
                    "Based on your materials, the answer would appear here.");
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(hfToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = Collections.singletonMap("inputs", prompt);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(hfUrl, request, String.class);
            return response.getBody();

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("error", "AI Service Unavailable", "fallback_answer", "[Simulated] Unable to reach AI provider. Please check logs.");
        }
    }
}

package com.edunexus.backend.controller;

import com.edunexus.backend.dto.MaterialRequest;
import com.edunexus.backend.entity.Material;
import com.edunexus.backend.service.AcademicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AcademicController {

    private final AcademicService academicService;

    @GetMapping("/academic/subjects")
    public ResponseEntity<?> getSubjects(@RequestParam String email) {
        try {
            return ResponseEntity.ok(academicService.getSubjects(email));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/academic/materials/{subjectId}")
    public ResponseEntity<?> getMaterials(@PathVariable Long subjectId) {
        return ResponseEntity.ok(academicService.getMaterials(subjectId));
    }

    @PostMapping("/admin/material")
    public ResponseEntity<?> addMaterial(@RequestBody MaterialRequest request) {
        return ResponseEntity.ok(academicService.addMaterial(request));
    }

    @PostMapping(value = "/admin/upload", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadMaterial(
            @RequestParam Long subjectId,
            @RequestParam Material.Type type,
            @RequestParam(required = false) String description,
            @RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(academicService.addMaterialFromPdf(subjectId, type, description, file));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error processing file: " + e.getMessage());
        }
    }
}

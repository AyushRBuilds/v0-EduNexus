package com.edunexus.backend.service;

import com.edunexus.backend.dto.MaterialRequest;
import com.edunexus.backend.entity.Material;
import com.edunexus.backend.entity.Subject;
import com.edunexus.backend.entity.User;
import com.edunexus.backend.repository.MaterialRepository;
import com.edunexus.backend.repository.SubjectRepository;
import com.edunexus.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademicService {

    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final MaterialRepository materialRepository;

    public User login(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<Subject> getSubjects(String email) {
        User user = login(email);
        if (user.getRole() == User.Role.STUDENT) {
            return subjectRepository.findByDepartmentAndSemester(user.getDepartment(), user.getSemester());
        }
        // Teachers/Admins see all
        return subjectRepository.findAll();
    }

    public List<Material> getMaterials(Long subjectId) {
        return materialRepository.findBySubjectId(subjectId);
    }

    public Material addMaterial(MaterialRequest req) {
        Subject subject = subjectRepository.findById(req.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Material material = new Material();
        material.setSubject(subject);
        material.setType(req.getType());
        material.setFilePath(req.getFilePath());
        material.setDescription(req.getDescription());
        material.setContent(req.getContent());

        return materialRepository.save(material);
    }

    public Material addMaterialFromPdf(Long subjectId, Material.Type type, String description, MultipartFile file) throws IOException {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        // Extract text
        String content = "";
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            content = stripper.getText(document);
            // NOTE: PDFBox won't extract text from images (scanned PDFs). OCR library required for that.
        }

        // Truncate if too large
        if (content.length() > 5000) {
            content = content.substring(0, 5000) + "...(truncated)";
        }

        Material material = new Material();
        material.setSubject(subject);
        material.setType(type);
        material.setFilePath(file.getOriginalFilename());
        material.setDescription(description);
        material.setContent(content);

        return materialRepository.save(material);
    }
}
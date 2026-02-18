package com.edunexus.backend.dto;

import com.edunexus.backend.entity.Material;
import lombok.Data;

@Data
public class MaterialRequest {
    private Long subjectId;
    private Material.Type type;
    private String filePath;
    private String description;
    private String content;
}

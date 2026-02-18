package com.edunexus.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Enumerated(EnumType.STRING)
    private Type type;

    private String filePath;
    private String description;

    @Column(columnDefinition = "TEXT")
    private String content;

    public enum Type {
        PDF, LINK, VIDEO
    }
}

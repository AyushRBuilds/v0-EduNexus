package com.edunexus.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String department;
    private Integer semester;

    public Subject(String name, String department, Integer semester) {
        this.name = name;
        this.department = department;
        this.semester = semester;
    }
}
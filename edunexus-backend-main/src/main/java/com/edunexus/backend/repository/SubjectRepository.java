package com.edunexus.backend.repository;

import com.edunexus.backend.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByDepartmentAndSemester(String department, Integer semester);
}
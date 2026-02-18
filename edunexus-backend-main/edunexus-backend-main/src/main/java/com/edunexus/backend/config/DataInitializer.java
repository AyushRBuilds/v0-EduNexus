package com.edunexus.backend.config;

import com.edunexus.backend.entity.Material;
import com.edunexus.backend.entity.Subject;
import com.edunexus.backend.entity.User;
import com.edunexus.backend.repository.MaterialRepository;
import com.edunexus.backend.repository.SubjectRepository;
import com.edunexus.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final MaterialRepository materialRepository;

    @Override
    public void run(String... args) throws Exception {
        // Users
        User student = new User(null, "John Doe", "student@email.com", User.Role.STUDENT, "CS", 3);
        User teacher = new User(null, "Prof Smith", "teacher@email.com", User.Role.TEACHER, "CS", null);
        User admin = new User(null, "Admin User", "admin@email.com", User.Role.ADMIN, "AdminDept", null);
        userRepository.saveAll(Arrays.asList(student, teacher, admin));

        // Subjects
        Subject ds = new Subject("Data Structures", "CS", 3);
        Subject bee = new Subject("Basic Electrical Eng", "CS", 3);
        Subject math = new Subject("Engineering Maths", "CS", 3);
        subjectRepository.saveAll(Arrays.asList(ds, bee, math));

        // Materials
        Material m1 = new Material();
        m1.setSubject(ds);
        m1.setType(Material.Type.PDF);
        m1.setFilePath("ds_notes_ch1.pdf");
        m1.setDescription("Chapter 1: Arrays and Linked Lists");
        m1.setContent("A Linked List is a linear data structure, in which the elements are not stored at contiguous memory locations. The elements in a linked list are linked using pointers.");

        Material m2 = new Material();
        m2.setSubject(bee);
        m2.setType(Material.Type.LINK);
        m2.setFilePath("http://bee-pyq.com");
        m2.setDescription("2023 Previous Year Question");
        m2.setContent("Q1: What is KCL? Answer: Kirchhoff's Current Law states that the algebraic sum of currents entering a node is zero.");

        materialRepository.saveAll(Arrays.asList(m1, m2));

        System.out.println("------------------------------------------------");
        System.out.println("EduNexus Initialized. Ready to serve!");
        System.out.println("Login as student: student@email.com");
        System.out.println("------------------------------------------------");
    }
}

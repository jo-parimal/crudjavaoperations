package com.cyepro.crudoperation.dto;

import com.cyepro.crudoperation.model.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO {
    private Long teacherId;
    private String teacherName;
    private String email;
    private String phoneNumber;
    private LocalDate hireDate;
    // Optionally include related DTOs if desired for nested representations
    // private Set<ClassSubjectAssignmentDTO> classSubjectAssignments;

    // Constructor to convert Teacher entity to TeacherDTO
    public TeacherDTO(Teacher teacher) {
        this.teacherId = teacher.getTeacherId();
        this.teacherName = teacher.getTeacherName();
        this.email = teacher.getEmail();
        this.phoneNumber = teacher.getPhoneNumber();
        this.hireDate = teacher.getHireDate();
        // If you want to include nested DTOs, populate them here.
        // Example:
        // if (teacher.getClassSubjectAssignments() != null) {
        //     this.classSubjectAssignments = teacher.getClassSubjectAssignments().stream()
        //                                          .map(ClassSubjectAssignmentDTO::new)
        //                                          .collect(Collectors.toSet());
        // }
    }
}

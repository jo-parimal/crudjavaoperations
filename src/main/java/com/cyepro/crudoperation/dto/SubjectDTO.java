package com.cyepro.crudoperation.dto;

import com.cyepro.crudoperation.model.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDTO {
    private Long subjectId;
    private String subjectName;
    private String description;
    // Optionally include related DTOs if desired for nested representations
    // private Set<StudentEnrollmentDTO> studentEnrollments;
    // private Set<ClassSubjectAssignmentDTO> classSubjectAssignments;

    // Constructor to convert Subject entity to SubjectDTO
    public SubjectDTO(Subject subject) {
        this.subjectId = subject.getSubjectId();
        this.subjectName = subject.getSubjectName();
        this.description = subject.getDescription();
        // If you want to include nested DTOs, populate them here.
        // Be mindful of depth to avoid over-fetching.
        // Example:
        // if (subject.getStudentEnrollments() != null) {
        //     this.studentEnrollments = subject.getStudentEnrollments().stream()
        //                                     .map(StudentEnrollmentDTO::new)
        //                                     .collect(Collectors.toSet());
        // }
    }
}

package com.cyepro.crudoperation.dto;

import com.cyepro.crudoperation.model.StudentEnrollment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentEnrollmentDTO {
    private Long enrollmentId;
    private Long studentId;
    private String studentName; // Added for display
    private Long subjectId;
    private String subjectName; // Added for display
    private LocalDate enrollmentDate;

    // Constructor to convert StudentEnrollment entity to StudentEnrollmentDTO
    public StudentEnrollmentDTO(StudentEnrollment enrollment) {
        this.enrollmentId = enrollment.getEnrollmentId();
        this.studentId = enrollment.getStudent() != null ? enrollment.getStudent().getStudentId() : null;
        this.studentName = enrollment.getStudent() != null ? enrollment.getStudent().getStudentName() : null;
        this.subjectId = enrollment.getSubject() != null ? enrollment.getSubject().getSubjectId() : null;
        this.subjectName = enrollment.getSubject() != null ? enrollment.getSubject().getSubjectName() : null;
        this.enrollmentDate = enrollment.getEnrollmentDate();
    }
}

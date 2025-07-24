package com.cyepro.crudoperation.dto;

import com.cyepro.crudoperation.model.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Long studentId;
    private String studentName;
    private LocalDate dateOfBirth;
    private Student.Gender gender; // Using the enum directly
    private String className;
    // We can include a simplified view of enrollments or just their IDs if needed
    private Set<StudentEnrollmentDTO> studentEnrollments;

    // Constructor to convert Student entity to StudentDTO
    public StudentDTO(Student student) {
        this.studentId = student.getStudentId();
        this.studentName = student.getStudentName();
        this.dateOfBirth = student.getDateOfBirth();
        this.gender = student.getGender();
        this.className = student.getClassName();
        if (student.getStudentEnrollments() != null) {
            this.studentEnrollments = student.getStudentEnrollments().stream()
                                            .map(StudentEnrollmentDTO::new)
                                            .collect(Collectors.toSet());
        }
    }
}

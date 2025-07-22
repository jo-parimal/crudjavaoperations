package com.cyepro.crudoperation.model; // Updated package

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Class_Subject_Assignments", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"subject_id", "class_name"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassSubjectAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Long assignmentId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "class_name", nullable = false)
    private String className;

    @Column(name = "academic_year", length = 10)
    private String academicYear;
}

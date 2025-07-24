package com.cyepro.crudoperation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassSubjectAssignmentDTO {
    private Long assignmentId;
    private Long teacherId;
    private String teacherName; // Added for display
    private Long subjectId;
    private String subjectName; // Added for display
    private String className;
    private String academicYear;
}

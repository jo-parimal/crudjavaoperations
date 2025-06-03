package com.cyepro.crudoperation.dto; // Updated package

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentTeacherSubjectDTO {
    private String studentName;
    private String className;
    private String subjectName;
    private String sirName;
}
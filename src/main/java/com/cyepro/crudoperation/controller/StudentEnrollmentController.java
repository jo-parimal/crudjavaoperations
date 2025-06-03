package com.cyepro.crudoperation.controller; // Updated package

import com.cyepro.crudoperation.model.StudentEnrollment; // Updated import
import com.cyepro.crudoperation.service.StudentEnrollmentService; // Updated import
import com.cyepro.crudoperation.dto.StudentTeacherSubjectDTO; // Updated import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
public class StudentEnrollmentController {

    @Autowired
    private StudentEnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<StudentEnrollment> createEnrollment(@RequestBody Map<String, Object> payload) {
        try {
            Long studentId = Long.valueOf(payload.get("studentId").toString());
            Long subjectId = Long.valueOf(payload.get("subjectId").toString());
            LocalDate enrollmentDate = payload.get("enrollmentDate") != null ?
                                        LocalDate.parse(payload.get("enrollmentDate").toString()) : null;

            StudentEnrollment newEnrollment = enrollmentService.createEnrollment(studentId, subjectId, enrollmentDate);
            return new ResponseEntity<>(newEnrollment, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<StudentEnrollment>> getAllEnrollments() {
        List<StudentEnrollment> enrollments = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentEnrollment> getEnrollmentById(@PathVariable Long id) {
        return enrollmentService.getEnrollmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentEnrollment> updateEnrollment(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            Long studentId = Long.valueOf(payload.get("studentId").toString());
            Long subjectId = Long.valueOf(payload.get("subjectId").toString());
            LocalDate enrollmentDate = payload.get("enrollmentDate") != null ?
                                        LocalDate.parse(payload.get("enrollmentDate").toString()) : null;

            StudentEnrollment updatedEnrollment = enrollmentService.updateEnrollment(id, studentId, subjectId, enrollmentDate);
            return ResponseEntity.ok(updatedEnrollment);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity("Error updating enrollment: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/student-teacher-subject")
    public ResponseEntity<List<StudentTeacherSubjectDTO>> getStudentsRegisteredForSubjectWithTeacher() {
        List<StudentTeacherSubjectDTO> result = enrollmentService.getStudentsRegisteredForSubjectWithTeacher();
        return ResponseEntity.ok(result);
    }
}
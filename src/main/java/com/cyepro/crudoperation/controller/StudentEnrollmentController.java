package com.cyepro.crudoperation.controller;

import com.cyepro.crudoperation.dto.StudentEnrollmentDTO; // New import
import com.cyepro.crudoperation.model.StudentEnrollment;
import com.cyepro.crudoperation.service.StudentEnrollmentService;
import com.cyepro.crudoperation.dto.StudentTeacherSubjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors; // New import

@RestController
@RequestMapping("/api/enrollments")
public class StudentEnrollmentController {

    @Autowired
    private StudentEnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<StudentEnrollmentDTO> createEnrollment(@RequestBody Map<String, Object> payload) {
        try {
            Long studentId = Long.valueOf(payload.get("studentId").toString());
            Long subjectId = Long.valueOf(payload.get("subjectId").toString());
            LocalDate enrollmentDate = payload.get("enrollmentDate") != null ?
                    LocalDate.parse(payload.get("enrollmentDate").toString()) : null;

            // Service now returns DTO
            StudentEnrollmentDTO newEnrollmentDto = enrollmentService.createEnrollment(studentId, subjectId, enrollmentDate);
            return new ResponseEntity<>(newEnrollmentDto, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<StudentEnrollmentDTO>> getAllEnrollments() {
        // Service now returns DTO list
        List<StudentEnrollmentDTO> enrollments = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentEnrollmentDTO> getEnrollmentById(@PathVariable Long id) {
        // Service now returns Optional<DTO>
        return enrollmentService.getEnrollmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentEnrollmentDTO> updateEnrollment(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            Long studentId = Long.valueOf(payload.get("studentId").toString());
            Long subjectId = Long.valueOf(payload.get("subjectId").toString());
            LocalDate enrollmentDate = payload.get("enrollmentDate") != null ?
                    LocalDate.parse(payload.get("enrollmentDate").toString()) : null;

            // Service now returns DTO
            StudentEnrollmentDTO updatedEnrollmentDto = enrollmentService.updateEnrollment(id, studentId, subjectId, enrollmentDate);
            return ResponseEntity.ok(updatedEnrollmentDto);
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

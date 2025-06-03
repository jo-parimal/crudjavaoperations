package com.cyepro.crudoperation.controller; // Updated package

import com.cyepro.crudoperation.model.ClassSubjectAssignment; // Updated import
import com.cyepro.crudoperation.service.ClassSubjectAssignmentService; // Updated import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assignments")
public class ClassSubjectAssignmentController {

    @Autowired
    private ClassSubjectAssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<ClassSubjectAssignment> createAssignment(@RequestBody Map<String, Object> payload) {
        try {
            Long teacherId = Long.valueOf(payload.get("teacherId").toString());
            Long subjectId = Long.valueOf(payload.get("subjectId").toString());
            String className = (String) payload.get("className");
            String academicYear = (String) payload.get("academicYear");

            ClassSubjectAssignment newAssignment = assignmentService.createAssignment(teacherId, subjectId, className, academicYear);
            return new ResponseEntity<>(newAssignment, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<ClassSubjectAssignment>> getAllAssignments() {
        List<ClassSubjectAssignment> assignments = assignmentService.getAllAssignments();
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassSubjectAssignment> getAssignmentById(@PathVariable Long id) {
        return assignmentService.getAssignmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassSubjectAssignment> updateAssignment(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            Long teacherId = Long.valueOf(payload.get("teacherId").toString());
            Long subjectId = Long.valueOf(payload.get("subjectId").toString());
            String className = (String) payload.get("className");
            String academicYear = (String) payload.get("academicYear");

            ClassSubjectAssignment updatedAssignment = assignmentService.updateAssignment(id, teacherId, subjectId, className, academicYear);
            return ResponseEntity.ok(updatedAssignment);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity("Error updating assignment: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
}
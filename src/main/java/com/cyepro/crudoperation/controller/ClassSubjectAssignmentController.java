package com.cyepro.crudoperation.controller;

import com.cyepro.crudoperation.dto.ClassSubjectAssignmentDTO; // New import
import com.cyepro.crudoperation.model.ClassSubjectAssignment;
import com.cyepro.crudoperation.service.ClassSubjectAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors; // New import

@RestController
@RequestMapping("/api/assignments")
public class ClassSubjectAssignmentController {

    @Autowired
    private ClassSubjectAssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<ClassSubjectAssignmentDTO> createAssignment(@RequestBody Map<String, Object> payload) {
        try {
            Long teacherId = Long.valueOf(payload.get("teacherId").toString());
            Long subjectId = Long.valueOf(payload.get("subjectId").toString());
            String className = (String) payload.get("className");
            String academicYear = (String) payload.get("academicYear");

            // Service now returns DTO
            ClassSubjectAssignmentDTO newAssignmentDto = assignmentService.createAssignment(teacherId, subjectId, className, academicYear);
            return new ResponseEntity<>(newAssignmentDto, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<ClassSubjectAssignmentDTO>> getAllAssignments() {
        // Service now returns DTO list
        List<ClassSubjectAssignmentDTO> assignments = assignmentService.getAllAssignments();
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassSubjectAssignmentDTO> getAssignmentById(@PathVariable Long id) {
        // Service now returns Optional<DTO>
        return assignmentService.getAssignmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassSubjectAssignmentDTO> updateAssignment(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            Long teacherId = Long.valueOf(payload.get("teacherId").toString());
            Long subjectId = Long.valueOf(payload.get("subjectId").toString());
            String className = (String) payload.get("className");
            String academicYear = (String) payload.get("academicYear");

            // Service now returns DTO
            ClassSubjectAssignmentDTO updatedAssignmentDto = assignmentService.updateAssignment(id, teacherId, subjectId, className, academicYear);
            return ResponseEntity.ok(updatedAssignmentDto);
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

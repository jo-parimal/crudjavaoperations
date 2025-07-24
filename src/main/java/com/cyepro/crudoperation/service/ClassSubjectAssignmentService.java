package com.cyepro.crudoperation.service;

import com.cyepro.crudoperation.dto.ClassSubjectAssignmentDTO; // New import
import com.cyepro.crudoperation.model.ClassSubjectAssignment;
import com.cyepro.crudoperation.model.Subject;
import com.cyepro.crudoperation.model.Teacher;
import com.cyepro.crudoperation.repository.ClassSubjectAssignmentRepository;
import com.cyepro.crudoperation.repository.SubjectRepository;
import com.cyepro.crudoperation.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // New import

@Service
public class ClassSubjectAssignmentService {

    @Autowired
    private ClassSubjectAssignmentRepository assignmentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    @Transactional
    public ClassSubjectAssignmentDTO createAssignment(Long teacherId, Long subjectId, String className, String academicYear) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + teacherId));
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + subjectId));

        if (assignmentRepository.findBySubjectAndClassName(subject, className).isPresent()) {
            throw new RuntimeException("Subject '" + subject.getSubjectName() + "' is already assigned to class '" + className + "'.");
        }

        ClassSubjectAssignment assignment = new ClassSubjectAssignment();
        assignment.setTeacher(teacher);
        assignment.setSubject(subject);
        assignment.setClassName(className);
        assignment.setAcademicYear(academicYear);
        ClassSubjectAssignment savedAssignment = assignmentRepository.save(assignment);
        // Convert to DTO before returning
        return new ClassSubjectAssignmentDTO(
                savedAssignment.getAssignmentId(),
                savedAssignment.getTeacher().getTeacherId(),
                savedAssignment.getTeacher().getTeacherName(),
                savedAssignment.getSubject().getSubjectId(),
                savedAssignment.getSubject().getSubjectName(),
                savedAssignment.getClassName(),
                savedAssignment.getAcademicYear()
        );
    }

    public List<ClassSubjectAssignmentDTO> getAllAssignments() {
        return assignmentRepository.findAll().stream()
                .map(assignment -> new ClassSubjectAssignmentDTO(
                        assignment.getAssignmentId(),
                        assignment.getTeacher().getTeacherId(),
                        assignment.getTeacher().getTeacherName(),
                        assignment.getSubject().getSubjectId(),
                        assignment.getSubject().getSubjectName(),
                        assignment.getClassName(),
                        assignment.getAcademicYear()
                ))
                .collect(Collectors.toList());
    }

    public Optional<ClassSubjectAssignmentDTO> getAssignmentById(Long id) {
        return assignmentRepository.findById(id)
                .map(assignment -> new ClassSubjectAssignmentDTO(
                        assignment.getAssignmentId(),
                        assignment.getTeacher().getTeacherId(),
                        assignment.getTeacher().getTeacherName(),
                        assignment.getSubject().getSubjectId(),
                        assignment.getSubject().getSubjectName(),
                        assignment.getClassName(),
                        assignment.getAcademicYear()
                ));
    }

    @Transactional
    public ClassSubjectAssignmentDTO updateAssignment(Long id, Long teacherId, Long subjectId, String className, String academicYear) {
        ClassSubjectAssignment existingAssignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class Subject Assignment not found with id: " + id));

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + teacherId));
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + subjectId));

        if (!existingAssignment.getSubject().getSubjectId().equals(subjectId) || !existingAssignment.getClassName().equals(className)) {
            if (assignmentRepository.findBySubjectAndClassName(subject, className).isPresent()) {
                throw new RuntimeException("A different assignment already exists for subject '" + subject.getSubjectName() + "' in class '" + className + "'.");
            }
        }

        existingAssignment.setTeacher(teacher);
        existingAssignment.setSubject(subject);
        existingAssignment.setClassName(className);
        existingAssignment.setAcademicYear(academicYear);
        ClassSubjectAssignment updatedAssignment = assignmentRepository.save(existingAssignment);
        // Convert to DTO before returning
        return new ClassSubjectAssignmentDTO(
                updatedAssignment.getAssignmentId(),
                updatedAssignment.getTeacher().getTeacherId(),
                updatedAssignment.getTeacher().getTeacherName(),
                updatedAssignment.getSubject().getSubjectId(),
                updatedAssignment.getSubject().getSubjectName(),
                updatedAssignment.getClassName(),
                updatedAssignment.getAcademicYear()
        );
    }

    public void deleteAssignment(Long id) {
        assignmentRepository.deleteById(id);
    }
}

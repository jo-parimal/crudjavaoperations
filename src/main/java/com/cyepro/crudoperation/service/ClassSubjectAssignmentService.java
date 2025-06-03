package com.cyepro.crudoperation.service; // Updated package

import com.cyepro.crudoperation.model.ClassSubjectAssignment; // Updated import
import com.cyepro.crudoperation.model.Subject; // Updated import
import com.cyepro.crudoperation.model.Teacher; // Updated import
import com.cyepro.crudoperation.repository.ClassSubjectAssignmentRepository; // Updated import
import com.cyepro.crudoperation.repository.SubjectRepository; // Updated import
import com.cyepro.crudoperation.repository.TeacherRepository; // Updated import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClassSubjectAssignmentService {

    @Autowired
    private ClassSubjectAssignmentRepository assignmentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    @Transactional
    public ClassSubjectAssignment createAssignment(Long teacherId, Long subjectId, String className, String academicYear) {
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
        return assignmentRepository.save(assignment);
    }

    public List<ClassSubjectAssignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public Optional<ClassSubjectAssignment> getAssignmentById(Long id) {
        return assignmentRepository.findById(id);
    }

    @Transactional
    public ClassSubjectAssignment updateAssignment(Long id, Long teacherId, Long subjectId, String className, String academicYear) {
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
        return assignmentRepository.save(existingAssignment);
    }

    public void deleteAssignment(Long id) {
        assignmentRepository.deleteById(id);
    }
}
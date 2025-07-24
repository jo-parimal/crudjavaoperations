package com.cyepro.crudoperation.service;

import com.cyepro.crudoperation.dto.StudentDTO; // New import
import com.cyepro.crudoperation.model.Student;
import com.cyepro.crudoperation.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // New import

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    public StudentDTO createStudent(Student student) {
        Student savedStudent = studentRepository.save(student);
        // Convert to DTO before returning
        return new StudentDTO(savedStudent);
    }

    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(StudentDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<StudentDTO> getStudentById(Long id) {
        return studentRepository.findById(id)
                .map(StudentDTO::new);
    }

    @Transactional
    public StudentDTO updateStudent(Long id, Student studentDetails) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        existingStudent.setStudentName(studentDetails.getStudentName());
        existingStudent.setDateOfBirth(studentDetails.getDateOfBirth());
        existingStudent.setGender(studentDetails.getGender());
        existingStudent.setClassName(studentDetails.getClassName());
        Student updatedStudent = studentRepository.save(existingStudent);
        // Convert to DTO before returning
        return new StudentDTO(updatedStudent);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}

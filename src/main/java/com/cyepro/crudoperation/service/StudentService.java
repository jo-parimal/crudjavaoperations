package com.cyepro.crudoperation.service;

import com.cyepro.crudoperation.dto.StudentDTO;
import com.cyepro.crudoperation.model.Student;
import com.cyepro.crudoperation.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Keep this import

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Transactional // Ensure this method is transactional to keep session open
    public StudentDTO createStudent(Student student) {
        Student savedStudent = studentRepository.save(student);
        // Convert to DTO before returning
        return new StudentDTO(savedStudent);
    }

    // THIS IS THE CRITICAL CHANGE FOR GET ALL STUDENTS
    @Transactional(readOnly = true) // Mark as read-only as we are only reading
    public List<StudentDTO> getAllStudents() {
        // Fetch all students. Ensure associated collections are initialized
        // A custom repository method with JOIN FETCH would be ideal for performance
        // For now, let's explicitly initialize within the transaction.
        List<Student> students = studentRepository.findAll();

        // Explicitly initialize the lazy collection for each student
        // This ensures the data is available before DTO conversion
        for (Student student : students) {
            // This line will trigger lazy loading of enrollments *within the active session*
            student.getStudentEnrollments().size(); // Accessing size forces initialization
        }

        return students.stream()
                .map(StudentDTO::new)
                .collect(Collectors.toList());
    }

    // THIS IS THE CRITICAL CHANGE FOR GET STUDENT BY ID
    @Transactional(readOnly = true) // Mark as read-only
    public Optional<StudentDTO> getStudentById(Long id) {
        return studentRepository.findById(id)
                .map(student -> {
                    // Explicitly initialize enrollments for the single student
                    student.getStudentEnrollments().size();
                    return new StudentDTO(student);
                });
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

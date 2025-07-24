package com.cyepro.crudoperation.service;

import com.cyepro.crudoperation.model.Student;
import com.cyepro.crudoperation.model.StudentEnrollment;
import com.cyepro.crudoperation.model.Subject;
import com.cyepro.crudoperation.repository.StudentEnrollmentRepository;
import com.cyepro.crudoperation.repository.StudentRepository;
import com.cyepro.crudoperation.repository.SubjectRepository;
import com.cyepro.crudoperation.dto.StudentEnrollmentDTO; // New import
import com.cyepro.crudoperation.dto.StudentTeacherSubjectDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentEnrollmentService {

    @Autowired
    private StudentEnrollmentRepository enrollmentRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public StudentEnrollmentDTO createEnrollment(Long studentId, Long subjectId, LocalDate enrollmentDate) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + subjectId));

        if (enrollmentRepository.findByStudentAndSubject(student, subject).isPresent()) {
            throw new RuntimeException("Student is already enrolled in this subject.");
        }

        StudentEnrollment enrollment = new StudentEnrollment();
        enrollment.setStudent(student);
        enrollment.setSubject(subject);
        enrollment.setEnrollmentDate(enrollmentDate != null ? enrollmentDate : LocalDate.now());
        StudentEnrollment savedEnrollment = enrollmentRepository.save(enrollment);
        // Convert to DTO before returning
        return new StudentEnrollmentDTO(savedEnrollment);
    }

    public List<StudentEnrollmentDTO> getAllEnrollments() {
        return enrollmentRepository.findAll().stream()
                .map(StudentEnrollmentDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<StudentEnrollmentDTO> getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id)
                .map(StudentEnrollmentDTO::new);
    }

    @Transactional
    public StudentEnrollmentDTO updateEnrollment(Long id, Long studentId, Long subjectId, LocalDate enrollmentDate) {
        StudentEnrollment existingEnrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student Enrollment not found with id: " + id));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + subjectId));

        if (!existingEnrollment.getStudent().getStudentId().equals(studentId) || !existingEnrollment.getSubject().getSubjectId().equals(subjectId)) {
            if (enrollmentRepository.findByStudentAndSubject(student, subject).isPresent()) {
                throw new RuntimeException("Student is already enrolled in this subject.");
            }
        }

        existingEnrollment.setStudent(student);
        existingEnrollment.setSubject(subject);
        existingEnrollment.setEnrollmentDate(enrollmentDate != null ? enrollmentDate : LocalDate.now());
        StudentEnrollment updatedEnrollment = enrollmentRepository.save(existingEnrollment);
        // Convert to DTO before returning
        return new StudentEnrollmentDTO(updatedEnrollment);
    }

    public void deleteEnrollment(Long id) {
        enrollmentRepository.deleteById(id);
    }

    @SuppressWarnings("unchecked")
    public List<StudentTeacherSubjectDTO> getStudentsRegisteredForSubjectWithTeacher() {
        String nativeQuery = "SELECT " +
                             "    S.student_name, " +
                             "    S.class_name, " +
                             "    Sub.subject_name, " +
                             "    T.teacher_name AS sir_name " +
                             "FROM " +
                             "    Students AS S " +
                             "JOIN " +
                             "    Student_Enrollments AS SE ON S.student_id = SE.student_id " +
                             "JOIN " +
                             "    Subjects AS Sub ON SE.subject_id = Sub.subject_id " +
                             "JOIN " +
                             "    Class_Subject_Assignments AS CSA ON Sub.subject_id = CSA.subject_id AND S.class_name = CSA.class_name " +
                             "JOIN " +
                             "    Teachers AS T ON CSA.teacher_id = T.teacher_id " +
                             "ORDER BY " +
                             "    S.student_name, Sub.subject_name";

        Query query = entityManager.createNativeQuery(nativeQuery);

        List<Object[]> results = query.getResultList();

        return results.stream().map(row -> new StudentTeacherSubjectDTO(
                (String) row[0],
                (String) row[1],
                (String) row[2],
                (String) row[3]
        )).collect(Collectors.toList());
    }
}

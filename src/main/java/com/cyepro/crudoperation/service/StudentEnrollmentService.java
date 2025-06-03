package com.cyepro.crudoperation.service; // Updated package

import com.cyepro.crudoperation.model.Student; // Updated import
import com.cyepro.crudoperation.model.StudentEnrollment; // Updated import
import com.cyepro.crudoperation.model.Subject; // Updated import
import com.cyepro.crudoperation.repository.StudentEnrollmentRepository; // Updated import
import com.cyepro.crudoperation.repository.StudentRepository; // Updated import
import com.cyepro.crudoperation.repository.SubjectRepository; // Updated import
import com.cyepro.crudoperation.dto.StudentTeacherSubjectDTO; // Updated import
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
    public StudentEnrollment createEnrollment(Long studentId, Long subjectId, LocalDate enrollmentDate) {
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
        return enrollmentRepository.save(enrollment);
    }

    public List<StudentEnrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    public Optional<StudentEnrollment> getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id);
    }

    @Transactional
    public StudentEnrollment updateEnrollment(Long id, Long studentId, Long subjectId, LocalDate enrollmentDate) {
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
        return enrollmentRepository.save(existingEnrollment);
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
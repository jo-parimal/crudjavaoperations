package com.cyepro.crudoperation.repository; // Updated package

import com.cyepro.crudoperation.model.StudentEnrollment; // Updated import
import com.cyepro.crudoperation.model.Student; // Updated import
import com.cyepro.crudoperation.model.Subject; // Updated import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentEnrollmentRepository extends JpaRepository<StudentEnrollment, Long> {
    Optional<StudentEnrollment> findByStudentAndSubject(Student student, Subject subject);
}
package com.cyepro.crudoperation.repository; // Updated package

import com.cyepro.crudoperation.model.ClassSubjectAssignment; // Updated import
import com.cyepro.crudoperation.model.Subject; // Updated import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassSubjectAssignmentRepository extends JpaRepository<ClassSubjectAssignment, Long> {
    Optional<ClassSubjectAssignment> findBySubjectAndClassName(Subject subject, String className);
}
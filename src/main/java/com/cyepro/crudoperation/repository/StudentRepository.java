package com.cyepro.crudoperation.repository; // Updated package

import com.cyepro.crudoperation.model.Student; // Updated import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
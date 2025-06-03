package com.cyepro.crudoperation.service;
import com.cyepro.crudoperation.model.Teacher; // Updated import
import com.cyepro.crudoperation.repository.TeacherRepository; // Updated import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service

public class TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;

    @Transactional
    public Teacher createTeacher(Teacher teacher) {
        if (teacher.getEmail() != null && teacherRepository.findByEmail(teacher.getEmail()).isPresent()) {
            throw new RuntimeException("Teacher with email '" + teacher.getEmail() + "' already exists.");
        }
        return teacherRepository.save(teacher);
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    @Transactional
    public Teacher updateTeacher(Long id, Teacher teacherDetails) {
        Teacher existingTeacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));

        if (teacherDetails.getEmail() != null && !existingTeacher.getEmail().equals(teacherDetails.getEmail())) {
            if (teacherRepository.findByEmail(teacherDetails.getEmail()).isPresent()) {
                throw new RuntimeException("Teacher with email '" + teacherDetails.getEmail() + "' already exists.");
            }
        }

        existingTeacher.setTeacherName(teacherDetails.getTeacherName());
        existingTeacher.setEmail(teacherDetails.getEmail());
        existingTeacher.setPhoneNumber(teacherDetails.getPhoneNumber());
        existingTeacher.setHireDate(teacherDetails.getHireDate());
        return teacherRepository.save(existingTeacher);
    }

    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }
}

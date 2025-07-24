package com.cyepro.crudoperation.service;

import com.cyepro.crudoperation.dto.TeacherDTO; // New import
import com.cyepro.crudoperation.model.Teacher;
import com.cyepro.crudoperation.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // New import

@Service
public class TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;

    @Transactional
    public TeacherDTO createTeacher(Teacher teacher) {
        if (teacher.getEmail() != null && teacherRepository.findByEmail(teacher.getEmail()).isPresent()) {
            throw new RuntimeException("Teacher with email '" + teacher.getEmail() + "' already exists.");
        }
        Teacher savedTeacher = teacherRepository.save(teacher);
        // Convert to DTO before returning
        return new TeacherDTO(savedTeacher);
    }

    public List<TeacherDTO> getAllTeachers() {
        return teacherRepository.findAll().stream()
                .map(TeacherDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<TeacherDTO> getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .map(TeacherDTO::new);
    }

    @Transactional
    public TeacherDTO updateTeacher(Long id, Teacher teacherDetails) {
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
        Teacher updatedTeacher = teacherRepository.save(existingTeacher);
        // Convert to DTO before returning
        return new TeacherDTO(updatedTeacher);
    }

    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }
}

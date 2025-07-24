package com.cyepro.crudoperation.service;

import com.cyepro.crudoperation.dto.SubjectDTO; // New import
import com.cyepro.crudoperation.model.Subject;
import com.cyepro.crudoperation.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // New import

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Transactional
    public SubjectDTO createSubject(Subject subject) {
        if (subjectRepository.findBySubjectName(subject.getSubjectName()).isPresent()) {
            throw new RuntimeException("Subject with name '" + subject.getSubjectName() + "' already exists.");
        }
        Subject savedSubject = subjectRepository.save(subject);
        // Convert to DTO before returning
        return new SubjectDTO(savedSubject);
    }

    public List<SubjectDTO> getAllSubjects() {
        return subjectRepository.findAll().stream()
                .map(SubjectDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<SubjectDTO> getSubjectById(Long id) {
        return subjectRepository.findById(id)
                .map(SubjectDTO::new);
    }

    @Transactional
    public SubjectDTO updateSubject(Long id, Subject subjectDetails) {
        Subject existingSubject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));

        if (!existingSubject.getSubjectName().equals(subjectDetails.getSubjectName())) {
            if (subjectRepository.findBySubjectName(subjectDetails.getSubjectName()).isPresent()) {
                throw new RuntimeException("Subject with name '" + subjectDetails.getSubjectName() + "' already exists.");
            }
        }
        existingSubject.setSubjectName(subjectDetails.getSubjectName());
        existingSubject.setDescription(subjectDetails.getDescription());
        Subject updatedSubject = subjectRepository.save(existingSubject);
        // Convert to DTO before returning
        return new SubjectDTO(updatedSubject);
    }

    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }
}

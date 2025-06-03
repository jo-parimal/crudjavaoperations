package com.cyepro.crudoperation.service; // Updated package

import com.cyepro.crudoperation.model.Subject; // Updated import
import com.cyepro.crudoperation.repository.SubjectRepository; // Updated import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Transactional
    public Subject createSubject(Subject subject) {
        if (subjectRepository.findBySubjectName(subject.getSubjectName()).isPresent()) {
            throw new RuntimeException("Subject with name '" + subject.getSubjectName() + "' already exists.");
        }
        return subjectRepository.save(subject);
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Optional<Subject> getSubjectById(Long id) {
        return subjectRepository.findById(id);
    }

    @Transactional
    public Subject updateSubject(Long id, Subject subjectDetails) {
        Subject existingSubject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));

        if (!existingSubject.getSubjectName().equals(subjectDetails.getSubjectName())) {
            if (subjectRepository.findBySubjectName(subjectDetails.getSubjectName()).isPresent()) {
                throw new RuntimeException("Subject with name '" + subjectDetails.getSubjectName() + "' already exists.");
            }
        }
        existingSubject.setSubjectName(subjectDetails.getSubjectName());
        existingSubject.setDescription(subjectDetails.getDescription());
        return subjectRepository.save(existingSubject);
    }

    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }
}
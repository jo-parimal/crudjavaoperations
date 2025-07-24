package com.cyepro.crudoperation.model;

import jakarta.persistence.*;
import lombok.Getter; // Changed from @Data to @Getter and @Setter
import lombok.Setter; // Changed from @Data to @Getter and @Setter
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.proxy.HibernateProxy; // Import for HibernateProxy

import java.util.HashSet;
import java.util.Objects; // For Objects.equals and Objects.hash
import java.util.Set;

@Entity
@Table(name = "Subjects")
@Getter // Use @Getter and @Setter explicitly
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "subject_name", nullable = false, unique = true)
    private String subjectName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    // @com.fasterxml.jackson.annotation.JsonManagedReference // No longer strictly needed on entity
    private Set<StudentEnrollment> studentEnrollments = new HashSet<>();

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    // @com.fasterxml.jackson.annotation.JsonManagedReference // No longer strictly needed on entity
    private Set<ClassSubjectAssignment> classSubjectAssignments = new HashSet<>();

    // IMPORTANT: Custom equals and hashCode for JPA entities
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (o instanceof HibernateProxy) {
            o = ((HibernateProxy) o).getHibernateLazyInitializer().getImplementation();
            if (this == o) return true;
        }
        Subject subject = (Subject) o;
        // Compare by ID if available, otherwise by unique business key (subjectName)
        if (subjectId != null && subject.subjectId != null) {
            return Objects.equals(subjectId, subject.subjectId);
        }
        return Objects.equals(subjectName, subject.subjectName); // Subject name is unique
    }

    @Override
    public int hashCode() {
        // If ID is available, use it. Otherwise, use the unique business key (subjectName).
        return Objects.hash(subjectId != null ? subjectId : subjectName);
    }
}

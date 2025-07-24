package com.cyepro.crudoperation.model;

import jakarta.persistence.*;
import lombok.Getter; // Changed from @Data to @Getter and @Setter
import lombok.Setter; // Changed from @Data to @Getter and @Setter
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.proxy.HibernateProxy; // Import for HibernateProxy

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Students")
@Getter // Use @Getter and @Setter explicitly
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", columnDefinition = "ENUM('Male', 'Female', 'Other')")
    private Gender gender;

    @Column(name = "class_name", nullable = false)
    private String className;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    // @com.fasterxml.jackson.annotation.JsonManagedReference // No longer strictly needed on entity
    private Set<StudentEnrollment> studentEnrollments = new HashSet<>();

    public enum Gender {
        Male, Female, Other
    }

    // IMPORTANT: Custom equals and hashCode for JPA entities
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (o instanceof HibernateProxy) {
            o = ((HibernateProxy) o).getHibernateLazyInitializer().getImplementation();
            if (this == o) return true;
        }
        Student student = (Student) o;
        // Compare by ID if available
        return studentId != null && Objects.equals(studentId, student.studentId);
    }

    @Override
    public int hashCode() {
        // Use ID for hashCode if available, otherwise a constant for new entities
        return studentId != null ? Objects.hash(studentId) : 31; // A constant is safer for new entities
    }
}

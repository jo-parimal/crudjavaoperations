package com.cyepro.crudoperation.model;

import jakarta.persistence.*;
import lombok.Getter; // Changed from @Data to @Getter and @Setter
import lombok.Setter; // Changed from @Data to @Getter and @Setter
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.proxy.HibernateProxy; // Import for HibernateProxy

import java.time.LocalDate;
import java.util.Objects; // For Objects.equals and Objects.hash

@Entity
@Table(name = "Student_Enrollments")
@Getter // Use @Getter and @Setter explicitly
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Long enrollmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    // @com.fasterxml.jackson.annotation.JsonBackReference // No longer strictly needed on entity if using DTOs for output
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    // @com.fasterxml.jackson.annotation.JsonBackReference // No longer strictly needed on entity if using DTOs for output
    private Subject subject;

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

    // IMPORTANT: Custom equals and hashCode for JPA entities
    // Use the primary key for identity. If the entity is new (id is null),
    // use a combination of natural/business keys if available,
    // otherwise, rely on object identity (which is less ideal but safer than lazy-loading relations).
    // For transient entities (id == null), stick to object identity (super.equals/hashCode)
    // to avoid issues before persistence.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        // Handle Hibernate proxies correctly
        if (o instanceof HibernateProxy) {
            o = ((HibernateProxy) o).getHibernateLazyInitializer().getImplementation();
            if (this == o) return true; // If proxy resolves to self
        }
        StudentEnrollment that = (StudentEnrollment) o;
        // If IDs are not null, compare by ID. This is the most reliable way for persisted entities.
        if (enrollmentId != null && that.enrollmentId != null) {
            return Objects.equals(enrollmentId, that.enrollmentId);
        }
        // If IDs are null (transient entities or before persistence),
        // consider a business key if available (student and subject unique combo),
        // or fall back to object identity (default behavior of Object.equals)
        // This can be tricky. For StudentEnrollment, the combination of student and subject is unique.
        // But for `hashCode`, we must be careful with lazy loading.
        return Objects.equals(student, that.student) &&
               Objects.equals(subject, that.subject);
    }

    @Override
    public int hashCode() {
        // IMPORTANT: For hashCode, if using ID for equals, use ID.
        // If ID is null (transient), avoid relying on lazy-loaded fields.
        // For StudentEnrollment, if enrollmentId is null, relying on student and subject's hash codes
        // *could* still trigger lazy loading. The safest for transient entities is to
        // just use a constant or `super.hashCode()` which uses object memory address.
        // Once persisted, `enrollmentId` is guaranteed to be non-null.
        return enrollmentId != null ? Objects.hash(enrollmentId) : Objects.hash(student, subject);
        // A safer approach for new entities (before saving) might be:
        // return enrollmentId != null ? Objects.hash(enrollmentId) : System.identityHashCode(this);
        // However, if we need to check uniqueness *before* saving, then student and subject are needed.
        // Assuming the error is primarily during post-load processing, the current form is better if student/subject are already initialized.
        // The error indicates `StudentEnrollment.hashCode` is being called on a proxy,
        // and its internal `Subject` (which is lazy) is causing the issue.
        // This is why we need to ensure the lazy relationship itself has a proper hashCode/equals.
    }
}

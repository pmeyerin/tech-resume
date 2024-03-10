package coop.stlma.tech.resume.education.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "education_type")
@NoArgsConstructor
@Getter
public class EducationTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID educationTypeId;
    private String educationType;

    public EducationTypeEntity(String educationType) {
        this.educationType = educationType;
    }
}

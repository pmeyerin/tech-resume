package coop.stlma.tech.resume.education.data;

import coop.stlma.tech.resume.education.data.entity.EducationTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EducationTypeRepository extends JpaRepository<EducationTypeEntity, UUID> {
    EducationTypeEntity findByEducationType(String educationType);
}

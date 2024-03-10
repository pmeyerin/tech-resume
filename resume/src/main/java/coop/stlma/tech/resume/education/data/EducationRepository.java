package coop.stlma.tech.resume.education.data;

import coop.stlma.tech.resume.education.data.entity.EducationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EducationRepository extends JpaRepository<EducationEntity, UUID> {

    @Query("" +
            "SELECT e " +
            "FROM education e " +
            "INNER JOIN  e.techsAndSkills t " +
            "WHERE e.worker = ?1 " +
            "AND t.techSkill.techSkillId = ?2")
    List<EducationEntity> findByWorkerAndSkill(UUID workerId, UUID skillId);
}

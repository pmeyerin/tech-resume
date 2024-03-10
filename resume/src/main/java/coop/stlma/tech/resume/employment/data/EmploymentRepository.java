package coop.stlma.tech.resume.employment.data;

import coop.stlma.tech.resume.employment.data.entity.EmploymentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmploymentRepository extends CrudRepository<EmploymentEntity, UUID> {

    @Query("" +
            "SELECT e " +
            "FROM employment e " +
            "INNER JOIN  e.techsAndSkills t " +
            "WHERE e.worker = ?1 " +
            "AND t.techSkill.techSkillId = ?2")
    List<EmploymentEntity> findByWorkerAndSkill(UUID workerId, UUID skillId);
}

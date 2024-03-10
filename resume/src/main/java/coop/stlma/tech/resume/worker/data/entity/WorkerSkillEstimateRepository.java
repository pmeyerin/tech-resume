package coop.stlma.tech.resume.worker.data.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkerSkillEstimateRepository extends JpaRepository<WorkerTechSkillEstimateEntity, UUID> {
    void deleteByWorker(UUID workerId);
}

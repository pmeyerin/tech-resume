package coop.stlma.tech.resume.worker.data;

import coop.stlma.tech.resume.worker.data.entity.WorkerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkerRepository extends JpaRepository<WorkerEntity, UUID> {
}

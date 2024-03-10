package coop.stlma.tech.resume.worker.data.entity;

import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity(name = "worker_skill_estimate")
@Data
public class WorkerTechSkillEstimateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID workerSkillEstimateId;

    private UUID worker;

    private int skillDaysEstimate;

    @ManyToOne
    @JoinColumn(name = "tech_skill_id", referencedColumnName = "techSkillId")
    private TechAndSkillEntity techSkill;
}

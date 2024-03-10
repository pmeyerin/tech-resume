package coop.stlma.tech.resume.worker.data.entity;

import coop.stlma.tech.resume.education.data.entity.EducationEntity;
import coop.stlma.tech.resume.employment.data.entity.EmploymentEntity;
import coop.stlma.tech.resume.project.data.entity.ProjectEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "worker")
@Data
public class WorkerEntity {

    public static final int WORKER_PROJECT_TYPE = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID workerId;

    private String workerName;

    private String workerPhone;

    private String workerEmail;

    @JoinColumn(name = "worker", referencedColumnName = "workerId")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<EmploymentEntity> employmentHistory = new ArrayList<>();

    @JoinColumn(name = "worker", referencedColumnName = "workerId")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<EducationEntity> educationHistory = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "project_relation", referencedColumnName = "workerId")
    @Where(clause = "project_relation_type = " + WORKER_PROJECT_TYPE)
    private List<ProjectEntity> projects = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "worker", referencedColumnName = "workerId")
    @OrderBy("skillDaysEstimate DESC")
    private List<WorkerTechSkillEstimateEntity> techsAndSkillsEstimates = new ArrayList<>();
}

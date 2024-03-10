package coop.stlma.tech.resume.project.service;

import coop.stlma.tech.resume.education.data.EducationRepository;
import coop.stlma.tech.resume.education.data.entity.EducationEntity;
import coop.stlma.tech.resume.education.error.NoSuchEducationException;
import coop.stlma.tech.resume.employment.data.EmploymentRepository;
import coop.stlma.tech.resume.employment.data.entity.EmploymentEntity;
import coop.stlma.tech.resume.employment.error.NoSuchEmploymentException;
import coop.stlma.tech.resume.project.Project;
import coop.stlma.tech.resume.project.data.ProjectRepository;
import coop.stlma.tech.resume.project.data.entity.ProjectEntity;
import coop.stlma.tech.resume.project.error.NoSuchProjectException;
import coop.stlma.tech.resume.techandskill.TechAndSkill;
import coop.stlma.tech.resume.techandskill.data.TechSkillsRepository;
import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillEntity;
import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillRelationEntity;
import coop.stlma.tech.resume.util.EntityDtoMappingUtils;
import coop.stlma.tech.resume.worker.data.entity.WorkerEntity;
import coop.stlma.tech.resume.worker.service.WorkerSkillUpdateService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final TechSkillsRepository techSkillsRepository;
    private final WorkerSkillUpdateService workerSkillUpdateService;
    private final EducationRepository educationRepository;
    private final EmploymentRepository employmentRepository;

    public ProjectService(ProjectRepository projectRepository,
                          TechSkillsRepository techSkillsRepository,
                          WorkerSkillUpdateService workerSkillUpdateService,
                          EducationRepository educationRepository,
                          EmploymentRepository employmentRepository) {
        this.projectRepository = projectRepository;
        this.techSkillsRepository = techSkillsRepository;
        this.workerSkillUpdateService = workerSkillUpdateService;
        this.educationRepository = educationRepository;
        this.employmentRepository = employmentRepository;
    }

    public List<TechAndSkill> addSkillsToProject(UUID projectId, List<String> skills) {
        ProjectEntity saveMe = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchEmploymentException(projectId));
        //TODO: handle if a skill is added that does not exist. As it is the skill will just not be found by this query and
        //the method will continue as though it was not in the request
        List<TechAndSkillEntity> skillEntities = techSkillsRepository.findByTechSkillNameIn(skills);
        List<TechAndSkillRelationEntity> relations = skillEntities.stream()
                .map(techAndSkillEntity -> {
                    TechAndSkillRelationEntity relation = new TechAndSkillRelationEntity();
                    relation.setRelationType(ProjectEntity.PROJECT_RELATION_TYPE);
                    relation.setRelationId(saveMe.getProjectId());
                    relation.setTechSkill(techAndSkillEntity);
                    return relation;
                })
                .toList();
        if (saveMe.getTechsAndSkills() == null) {
            saveMe.setTechsAndSkills(new ArrayList<>());
        }
        saveMe.getTechsAndSkills().addAll(relations);
        projectRepository.save(saveMe);
        workerSkillUpdateService.updateWorkerSkillHistory(findProjectWorker(saveMe.getProjectId()));
        return skillEntities.stream().map(EntityDtoMappingUtils::buildTechsAndSkills).toList();
    }

    public UUID findProjectWorker(UUID projectId) {
        ProjectEntity findMyWorker = projectRepository.findById(projectId).orElseThrow(() -> new NoSuchProjectException(projectId));
        if (findMyWorker.getProjectRelationType() == WorkerEntity.WORKER_PROJECT_TYPE) {
            return findMyWorker.getProjectRelation();
        }
        else if (findMyWorker.getProjectRelationType() == EducationEntity.EDUCATION_RELATION_TYPE) {
            return educationRepository.findById(findMyWorker.getProjectRelation())
                    .map(EducationEntity::getWorker)
                    .orElseThrow(() -> new NoSuchEducationException(findMyWorker.getProjectRelation()));
        }
        else if (findMyWorker.getProjectRelationType() == EmploymentEntity.EMPLOYMENT_RELATION_TYPE) {
            return employmentRepository.findById(findMyWorker.getProjectRelation())
                    .map(EmploymentEntity::getWorker)
                    .orElseThrow(() -> new NoSuchEmploymentException(findMyWorker.getProjectRelation()));
        }
        else {
            throw new IllegalArgumentException("Unexpected value: " + findMyWorker.getProjectRelationType());
        }
    }

    public Project getProject(UUID projectId) {
        return projectRepository.findById(projectId)
                .map(EntityDtoMappingUtils::buildProject)
                .orElseThrow(() -> new NoSuchProjectException(projectId));
    }
}

package coop.stlma.tech.resume.employment.service;

import coop.stlma.tech.resume.employment.Employment;
import coop.stlma.tech.resume.employment.data.EmploymentRepository;
import coop.stlma.tech.resume.employment.data.entity.EmploymentEntity;
import coop.stlma.tech.resume.employment.error.NoSuchEmploymentException;
import coop.stlma.tech.resume.project.Project;
import coop.stlma.tech.resume.techandskill.TechAndSkill;
import coop.stlma.tech.resume.techandskill.data.TechSkillsRepository;
import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillEntity;
import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillRelationEntity;
import coop.stlma.tech.resume.util.EntityDtoMappingUtils;
import coop.stlma.tech.resume.worker.data.WorkerRepository;
import coop.stlma.tech.resume.worker.error.NoSuchWorkerException;
import coop.stlma.tech.resume.worker.service.WorkerSkillUpdateService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class EmploymentService {

    private final EmploymentRepository employmentRepository;
    private final TechSkillsRepository techSkillsRepository;
    private final WorkerSkillUpdateService workerSkillUpdateService;
    private final WorkerRepository workerRepository;

    public EmploymentService(EmploymentRepository employmentRepository,
                            TechSkillsRepository techSkillsRepository,
                            WorkerSkillUpdateService workerSkillUpdateService,
                            WorkerRepository workerRepository) {
        this.employmentRepository = employmentRepository;
        this.techSkillsRepository = techSkillsRepository;
        this.workerSkillUpdateService = workerSkillUpdateService;
        this.workerRepository = workerRepository;
    }

    public Employment addEmployment(UUID workerId, Employment employment) {
        workerRepository.findById(workerId)
                .orElseThrow(() -> new NoSuchWorkerException(workerId));
        EmploymentEntity saveMe = EntityDtoMappingUtils.buildEmploymentDto(workerId,
                employment);
        saveMe = employmentRepository.save(saveMe);
        return EntityDtoMappingUtils.buildEmployment(saveMe);
    }

    public List<TechAndSkill> addSkillsToEmployment(UUID employmentId, List<String> skills) {
        EmploymentEntity saveMe = employmentRepository.findById(employmentId)
                .orElseThrow(() -> new NoSuchEmploymentException(employmentId));
        //TODO: handle if a skill is added that does not exist. As it is the skill will just not be found by this query and
        //the method will continue as though it was not in the request
        List<TechAndSkillEntity> skillEntities = techSkillsRepository.findByTechSkillNameIn(skills);
        List<TechAndSkillRelationEntity> relations = skillEntities.stream()
                .map(techAndSkillEntity -> {
                    TechAndSkillRelationEntity relation = new TechAndSkillRelationEntity();
                    relation.setRelationType(EmploymentEntity.EMPLOYMENT_RELATION_TYPE);
                    relation.setRelationId(saveMe.getEmploymentId());
                    relation.setTechSkill(techAndSkillEntity);
                    return relation;
                })
                .toList();
        if (saveMe.getTechsAndSkills() == null) {
            saveMe.setTechsAndSkills(new ArrayList<>());
        }
        saveMe.getTechsAndSkills().addAll(relations);
        employmentRepository.save(saveMe);
        workerSkillUpdateService.updateWorkerSkillHistory(saveMe.getWorker());
        return skillEntities.stream().map(EntityDtoMappingUtils::buildTechsAndSkills).toList();
    }

    public Project addProjectToEmployment(UUID employmentId, Project project) {
        EmploymentEntity saveMe = employmentRepository.findById(employmentId)
                .orElseThrow(() -> new NoSuchEmploymentException(employmentId));
        if (saveMe.getProjects() == null) {
            saveMe.setProjects(new ArrayList<>());
        }
        saveMe.getProjects().add(EntityDtoMappingUtils.buildProjectEntity(project, EmploymentEntity.EMPLOYMENT_RELATION_TYPE));
        saveMe = employmentRepository.save(saveMe);
        //TODO: handle the case where a project already exists with that name for this employer
        return saveMe.getProjects().stream()
                .filter(projectEntity ->
                        projectEntity.getProjectName().equals(project.getProjectName()))
                .findFirst()
                .map(EntityDtoMappingUtils::buildProject)
                .orElse(null);
    }
}

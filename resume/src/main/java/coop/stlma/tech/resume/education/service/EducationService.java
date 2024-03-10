package coop.stlma.tech.resume.education.service;

import coop.stlma.tech.resume.education.data.EducationTypeRepository;
import coop.stlma.tech.resume.education.data.entity.EducationEntity;
import coop.stlma.tech.resume.education.error.NoSuchEducationException;
import coop.stlma.tech.resume.education.Education;
import coop.stlma.tech.resume.education.data.EducationRepository;
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
public class EducationService {

    private final EducationRepository educationRepository;
    private final EducationTypeRepository educationTypeRepository;
    private final TechSkillsRepository techSkillsRepository;
    private final WorkerSkillUpdateService workerSkillUpdateService;
    private final WorkerRepository workerRepository;

    public EducationService(EducationRepository educationRepository,
                            EducationTypeRepository educationTypeRepository,
                            TechSkillsRepository techSkillsRepository,
                            WorkerSkillUpdateService workerSkillUpdateService,
                            WorkerRepository workerRepository) {
        this.educationRepository = educationRepository;
        this.educationTypeRepository = educationTypeRepository;
        this.techSkillsRepository = techSkillsRepository;
        this.workerSkillUpdateService = workerSkillUpdateService;
        this.workerRepository = workerRepository;
    }

    public Education addEducation(UUID workerId, Education education) {
        workerRepository.findById(workerId)
                .orElseThrow(() -> new NoSuchWorkerException(workerId));
        EducationEntity saveMe = EntityDtoMappingUtils.buildEducationDto(workerId,
                education,
                educationTypeRepository.findByEducationType(education.getEducationType().getName()));
        saveMe = educationRepository.save(saveMe);
        return EntityDtoMappingUtils.buildEducation(saveMe);
    }

    public List<TechAndSkill> addSkillsToEducation(UUID educationId, List<String> skills) {
        EducationEntity saveMe = educationRepository.findById(educationId)
                .orElseThrow(() -> new NoSuchEducationException(educationId));
        //TODO: handle if a skill is added that does not exist. As it is the skill will just not be found by this query and
        //the method will continue as though it was not in the request
        List<TechAndSkillEntity> skillEntities = techSkillsRepository.findByTechSkillNameIn(skills);
        List<TechAndSkillRelationEntity> relations = skillEntities.stream()
            .map(techAndSkillEntity -> {
                TechAndSkillRelationEntity relation = new TechAndSkillRelationEntity();
                relation.setRelationType(EducationEntity.EDUCATION_RELATION_TYPE);
                relation.setRelationId(saveMe.getEducationId());
                relation.setTechSkill(techAndSkillEntity);
                return relation;
            })
            .toList();
        if (saveMe.getTechsAndSkills() == null) {
            saveMe.setTechsAndSkills(new ArrayList<>());
        }
        saveMe.getTechsAndSkills().addAll(relations);
        educationRepository.save(saveMe);
        workerSkillUpdateService.updateWorkerSkillHistory(saveMe.getWorker());
        return skillEntities.stream().map(EntityDtoMappingUtils::buildTechsAndSkills).toList();
    }

    public Project addProjectToEducation(UUID educationId, Project project) {
        EducationEntity saveMe = educationRepository.findById(educationId)
                .orElseThrow(() -> new NoSuchEducationException(educationId));
        if (saveMe.getProjects() == null) {
            saveMe.setProjects(new ArrayList<>());
        }
        saveMe.getProjects().add(EntityDtoMappingUtils.buildProjectEntity(project, EducationEntity.EDUCATION_RELATION_TYPE));
        saveMe = educationRepository.save(saveMe);
        //TODO: handle the case where a project already exists with that name for this institution
        return saveMe.getProjects().stream()
                .filter(projectEntity ->
                        projectEntity.getProjectName().equals(project.getProjectName()))
                .findFirst()
                .map(EntityDtoMappingUtils::buildProject)
                .orElse(null);
    }
}

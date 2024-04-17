package coop.stlma.tech.resume.techandskill.service;

import coop.stlma.tech.resume.education.Education;
import coop.stlma.tech.resume.education.data.EducationRepository;
import coop.stlma.tech.resume.employment.Employment;
import coop.stlma.tech.resume.employment.data.EmploymentRepository;
import coop.stlma.tech.resume.project.Project;
import coop.stlma.tech.resume.project.data.ProjectRepository;
import coop.stlma.tech.resume.techandskill.SkillGrouping;
import coop.stlma.tech.resume.techandskill.TechAndSkill;
import coop.stlma.tech.resume.techandskill.data.TechSkillsRepository;
import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillEntity;
import coop.stlma.tech.resume.util.EntityDtoMappingUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TechSkillsService {

    private final TechSkillsRepository techSkillsRepository;
    private final ProjectRepository projectRepository;
    private final EducationRepository educationRepository;
    private final EmploymentRepository employmentRepository;

    public TechSkillsService(TechSkillsRepository techSkillsRepository,
                             ProjectRepository projectRepository,
                             EducationRepository educationRepository,
                             EmploymentRepository employmentRepository
    ) {
        this.techSkillsRepository = techSkillsRepository;
        this.projectRepository = projectRepository;
        this.educationRepository = educationRepository;
        this.employmentRepository = employmentRepository;
    }

    public List<TechAndSkill> getAllTechSkills() {
        return techSkillsRepository.findAll().stream()
                .map(techAndSkillEntity -> TechAndSkill.builder()
                        .techSkillId(techAndSkillEntity.getTechSkillId())
                        .techSkillName(techAndSkillEntity.getTechSkillName())
                        .build()).collect(Collectors.toList());
    }

    public TechAndSkill getByName(String name) {
        return techSkillsRepository.findByTechSkillName(name)
                .map(techAndSkillEntity -> TechAndSkill.builder()
                        .techSkillId(techAndSkillEntity.getTechSkillId())
                        .techSkillName(techAndSkillEntity.getTechSkillName())
                        .build()).orElse(null);
    }

    public void bulkSave(List<String> techSkill)  {
        try {
            techSkillsRepository.saveAll(techSkill.stream()
                    .map(techSkillName -> {
                        TechAndSkillEntity techAndSkillEntity = new TechAndSkillEntity();
                        techAndSkillEntity.setTechSkillName(techSkillName);
                        return techAndSkillEntity;
                    }).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public SkillGrouping getByWorkerAndSkill(UUID workerId, UUID skillId) {
        List<Project> projects = projectRepository.findByWorkerAndSkill(workerId, skillId).stream()
                .map(EntityDtoMappingUtils::buildProject).toList();

        List<Employment> employmentProjects = employmentRepository.findByWorkerAndSkill(workerId, skillId).stream()
                .map(EntityDtoMappingUtils::buildEmployment).toList();

        List<Education> educationProjects = educationRepository.findByWorkerAndSkill(workerId, skillId).stream()
                .map(EntityDtoMappingUtils::buildEducation).toList();

        SkillGrouping skillGrouping = new SkillGrouping();
        skillGrouping.setProjects(projects);
        skillGrouping.setEmployments(employmentProjects);
        skillGrouping.setEducations(educationProjects);
        return skillGrouping;
    }
}

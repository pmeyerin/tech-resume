package coop.stlma.tech.resume.util;

import coop.stlma.tech.resume.education.Education;
import coop.stlma.tech.resume.education.data.EducationType;
import coop.stlma.tech.resume.education.data.entity.EducationEntity;
import coop.stlma.tech.resume.education.data.entity.EducationTypeEntity;
import coop.stlma.tech.resume.employment.Employment;
import coop.stlma.tech.resume.employment.data.entity.EmploymentEntity;
import coop.stlma.tech.resume.project.Project;
import coop.stlma.tech.resume.project.data.entity.ProjectEntity;
import coop.stlma.tech.resume.techandskill.TechAndSkill;
import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillEntity;
import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillRelationEntity;
import coop.stlma.tech.resume.worker.WorkerSkillEstimate;
import coop.stlma.tech.resume.worker.data.entity.WorkerTechSkillEstimateEntity;

import java.util.UUID;
import java.util.stream.Collectors;

public class EntityDtoMappingUtils {

    private EntityDtoMappingUtils(){}
    public static WorkerSkillEstimate buildWorkerSkillEstimates(WorkerTechSkillEstimateEntity workerTechSkillEstimateEntity) {
        return WorkerSkillEstimate.builder()
                .techSkillId(workerTechSkillEstimateEntity.getTechSkill().getTechSkillId())
                .techSkillName(workerTechSkillEstimateEntity.getTechSkill().getTechSkillName())
                .yearsSkillsEstimate(workerTechSkillEstimateEntity.getSkillDaysEstimate())
                .build();
    }

    public static Employment buildEmployment(EmploymentEntity entity) {
        return Employment.builder()
                .employmentId(entity.getEmploymentId())
                .employmentName(entity.getEmploymentName())
                .employmentDescription(entity.getEmploymentDescription())
                .employmentStart(entity.getEmploymentStart())
                .employmentEnd(entity.getEmploymentEnd())
                .projects(entity.getProjects().stream()
                        .map(EntityDtoMappingUtils::buildProject).toList())
                .techAndSkills(entity.getTechsAndSkills().stream()
                        .map(EntityDtoMappingUtils::buildTechsAndSkills).toList())
                .build();
    }
    public static Education buildEducation(EducationEntity educationEntity) {
        return Education.builder()
                .educationId(educationEntity.getEducationId())
                .educationType(EducationType.ofName(educationEntity.getEducationType().getEducationType()))
                .institutionName(educationEntity.getInstitutionName())
                .program(educationEntity.getProgram())
                .startDate(educationEntity.getStartDate())
                .graduationPeriod(educationEntity.getGraduationPeriod())
                .projects(educationEntity.getProjects().stream()
                        .map(EntityDtoMappingUtils::buildProject).toList())
                .techAndSkills(educationEntity.getTechsAndSkills().stream()
                        .map(EntityDtoMappingUtils::buildTechsAndSkills).toList())
                .build();
    }

    public static Project buildProject(ProjectEntity projectEntity) {
        return Project.builder()
                .projectId(projectEntity.getProjectId())
                .projectName(projectEntity.getProjectName())
                .projectDescription(projectEntity.getProjectDescription())
                .projectStart(projectEntity.getProjectStart())
                .projectEnd(projectEntity.getProjectEnd())
                .techAndSkills(projectEntity.getTechsAndSkills().stream()
                        .map(EntityDtoMappingUtils::buildTechsAndSkills).toList())
                .build();
    }

    public static TechAndSkill buildTechsAndSkills(TechAndSkillRelationEntity techAndSkillEntity) {
        return buildTechsAndSkills(techAndSkillEntity.getTechSkill());
    }

    public static TechAndSkill buildTechsAndSkills(TechAndSkillEntity techAndSkillEntity) {
        return TechAndSkill.builder()
                .techSkillId(techAndSkillEntity.getTechSkillId())
                .techSkillName(techAndSkillEntity.getTechSkillName())
                .build();
    }

    public static EducationEntity buildEducationDto(UUID workerId, Education dto, EducationTypeEntity type) {
        EducationEntity entity = new EducationEntity();
        entity.setWorker(workerId);
        entity.setEducationType(type);
        entity.setProgram(dto.getProgram());
        entity.setInstitutionName(dto.getInstitutionName());
        entity.setGraduationPeriod(dto.getGraduationPeriod());
        entity.setStartDate(dto.getStartDate());
        return entity;
    }

    public static ProjectEntity buildProjectEntity(Project project, int relationType) {
        ProjectEntity entity = new ProjectEntity();
        entity.setProjectId(project.getProjectId());
        entity.setProjectName(project.getProjectName());
        entity.setProjectDescription(project.getProjectDescription());
        entity.setProjectStart(project.getProjectStart());
        entity.setProjectEnd(project.getProjectEnd());
        entity.setProjectRelationType(relationType);
        return entity;
    }

    public static EmploymentEntity buildEmploymentDto(UUID workerId, Employment employment) {
        EmploymentEntity entity = new EmploymentEntity();
        entity.setWorker(workerId);
        entity.setEmploymentName(employment.getEmploymentName());
        entity.setEmploymentDescription(employment.getEmploymentDescription());
        entity.setEmploymentStart(employment.getEmploymentStart());
        entity.setEmploymentEnd(employment.getEmploymentEnd());
        return entity;
    }
}

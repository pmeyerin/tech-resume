package coop.stlma.tech.resume.worker.service;

import coop.stlma.tech.resume.education.data.entity.EducationEntity;
import coop.stlma.tech.resume.education.data.entity.EducationTypeEntity;
import coop.stlma.tech.resume.employment.data.entity.EmploymentEntity;
import coop.stlma.tech.resume.project.data.entity.ProjectEntity;
import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillEntity;
import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillRelationEntity;
import coop.stlma.tech.resume.worker.data.WorkerRepository;
import coop.stlma.tech.resume.worker.data.entity.WorkerEntity;
import coop.stlma.tech.resume.worker.data.entity.WorkerSkillEstimateRepository;
import coop.stlma.tech.resume.worker.data.entity.WorkerTechSkillEstimateEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest(classes = {WorkerSkillUpdateService.class})
public class WorkerSkillUpdateServiceTest {

    @Autowired
    WorkerSkillUpdateService testObject;

    @MockBean
    WorkerRepository workerRepository;

    @MockBean
    WorkerSkillEstimateRepository workerSkillEstimateRepository;

    @Test
    public void testUpdateWorkerSkills_happyPath() {
        TechAndSkillEntity javaSkill = new TechAndSkillEntity();
        javaSkill.setTechSkillId(UUID.nameUUIDFromBytes("java".getBytes()));
        javaSkill.setTechSkillName("java");
        TechAndSkillEntity pythonSkill = new TechAndSkillEntity();
        pythonSkill.setTechSkillId(UUID.nameUUIDFromBytes("python".getBytes()));
        pythonSkill.setTechSkillName("python");
        TechAndSkillRelationEntity javaRelation = new TechAndSkillRelationEntity();
        javaRelation.setTechSkillRelationId(UUID.nameUUIDFromBytes("java".getBytes()));
        javaRelation.setTechSkill(javaSkill);
        TechAndSkillRelationEntity pythonRelation = new TechAndSkillRelationEntity();
        pythonRelation.setTechSkillRelationId(UUID.nameUUIDFromBytes("python".getBytes()));
        pythonRelation.setTechSkill(pythonSkill);
        WorkerEntity one = new WorkerEntity();
        one.setWorkerId(UUID.nameUUIDFromBytes("one".getBytes()));
        one.setWorkerName("Some guy");
        one.setWorkerEmail("some@guy.com");
        EmploymentEntity oneJob = new EmploymentEntity();
        oneJob.setEmploymentId(UUID.nameUUIDFromBytes("oneJob".getBytes()));
        oneJob.setEmploymentName("some job");
        oneJob.setEmploymentDescription("My favorite job");
        oneJob.setEmploymentStart(LocalDate.of(2020, 2, 3));
        oneJob.setEmploymentEnd(LocalDate.of(2021, 3, 4));
        ProjectEntity oneJobProject = new ProjectEntity();
        oneJobProject.setProjectId(UUID.nameUUIDFromBytes("oneJobProject".getBytes()));
        oneJobProject.setProjectName("some project");
        oneJobProject.setProjectDescription("My favorite project");
        oneJobProject.setProjectStart(LocalDate.of(2020, 2, 4));
        oneJobProject.setProjectEnd(LocalDate.of(2021, 3, 3));
        oneJobProject.getTechsAndSkills().add(pythonRelation);
        oneJob.getProjects().add(oneJobProject);
        oneJob.getTechsAndSkills().addAll(List.of(javaRelation, pythonRelation));

        one.getEmploymentHistory().add(oneJob);

        EducationEntity oneEd = new EducationEntity();
        oneEd.setEducationId(UUID.nameUUIDFromBytes("oneEd".getBytes()));
        oneEd.setEducationType(new EducationTypeEntity("graduate"));
        oneEd.setProgram("prog");
        oneEd.setStartDate(LocalDate.of(2010, 2, 3));
        oneEd.setGraduationPeriod(LocalDate.of(2011, 5, 1));
        oneEd.setInstitutionName("My School");
        oneEd.getTechsAndSkills().add(pythonRelation);

        ProjectEntity oneEdProject = new ProjectEntity();
        oneEdProject.setProjectId(UUID.nameUUIDFromBytes("oneEdProject".getBytes()));
        oneEdProject.setProjectName("school project");
        oneEdProject.setProjectDescription("My school project");
        oneEdProject.setProjectStart(LocalDate.of(2010, 2, 4));
        oneEdProject.setProjectEnd(LocalDate.of(2011, 4, 10));
        oneEd.getProjects().add(oneEdProject);
        ProjectEntity oneEdProject2 = new ProjectEntity();
        oneEdProject2.setProjectId(UUID.nameUUIDFromBytes("oneEdProject2".getBytes()));
        oneEdProject2.setProjectName("schoolProj2");
        oneEdProject2.setProjectDescription("less cool school project");
        oneEdProject2.setProjectStart(LocalDate.of(2006, 11, 4));
        oneEdProject2.setProjectEnd(LocalDate.of(2010, 1, 31));
        oneEd.getProjects().add(oneEdProject2);

        one.getEducationHistory().add(oneEd);
        ProjectEntity oneProject = new ProjectEntity();
        oneProject.setProjectId(UUID.nameUUIDFromBytes("oneProject".getBytes()));
        oneProject.setProjectName("hobby project");
        oneProject.setProjectDescription("My hobby project");
        oneProject.setProjectStart(LocalDate.of(2020, 2, 4));
        oneProject.setProjectEnd(LocalDate.of(2021, 3, 3));
        one.getProjects().add(oneProject);

        Mockito.when(workerRepository.findById(one.getWorkerId())).thenReturn(Optional.of(one));

        testObject.updateWorkerSkillHistory(one.getWorkerId());

        WorkerEntity afterUpdate = workerRepository.findById(one.getWorkerId()).get();
        Assertions.assertEquals(2, afterUpdate.getTechsAndSkillsEstimates().size());
        WorkerTechSkillEstimateEntity workerPythonEstimate = afterUpdate.getTechsAndSkillsEstimates().stream()
                .filter(e -> e.getTechSkill().getTechSkillId().equals(pythonSkill.getTechSkillId())).findAny().get();
        WorkerTechSkillEstimateEntity workerJavaEstimate = afterUpdate.getTechsAndSkillsEstimates().stream()
                .filter(e -> e.getTechSkill().getTechSkillId().equals(javaSkill.getTechSkillId())).findAny().get();

        Assertions.assertEquals(847, workerPythonEstimate.getSkillDaysEstimate());
        Assertions.assertEquals(395, workerJavaEstimate.getSkillDaysEstimate());
    }

    @Test
    public void testUpdateWorkerSkills_ongoingWork() {
        TechAndSkillEntity javaSkill = new TechAndSkillEntity();
        javaSkill.setTechSkillId(UUID.nameUUIDFromBytes("java".getBytes()));
        javaSkill.setTechSkillName("java");
        TechAndSkillEntity pythonSkill = new TechAndSkillEntity();
        pythonSkill.setTechSkillId(UUID.nameUUIDFromBytes("python".getBytes()));
        pythonSkill.setTechSkillName("python");
        TechAndSkillRelationEntity javaRelation = new TechAndSkillRelationEntity();
        javaRelation.setTechSkillRelationId(UUID.nameUUIDFromBytes("java".getBytes()));
        javaRelation.setTechSkill(javaSkill);
        TechAndSkillRelationEntity pythonRelation = new TechAndSkillRelationEntity();
        pythonRelation.setTechSkillRelationId(UUID.nameUUIDFromBytes("python".getBytes()));
        pythonRelation.setTechSkill(pythonSkill);
        WorkerEntity one = new WorkerEntity();
        one.setWorkerId(UUID.nameUUIDFromBytes("one".getBytes()));
        one.setWorkerName("Some guy");
        one.setWorkerEmail("some@guy.com");
        EmploymentEntity oneJob = new EmploymentEntity();
        oneJob.setEmploymentId(UUID.nameUUIDFromBytes("oneJob".getBytes()));
        oneJob.setEmploymentName("some job");
        oneJob.setEmploymentDescription("My favorite job");
        oneJob.setEmploymentStart(LocalDate.of(2020, 2, 3));
        oneJob.setEmploymentEnd(null);
        ProjectEntity oneJobProject = new ProjectEntity();
        oneJobProject.setProjectId(UUID.nameUUIDFromBytes("oneJobProject".getBytes()));
        oneJobProject.setProjectName("some project");
        oneJobProject.setProjectDescription("My favorite project");
        oneJobProject.setProjectStart(LocalDate.of(2020, 2, 4));
        oneJobProject.setProjectEnd(null);
        oneJobProject.getTechsAndSkills().add(pythonRelation);
        oneJob.getProjects().add(oneJobProject);
        oneJob.getTechsAndSkills().addAll(List.of(javaRelation, pythonRelation));

        one.getEmploymentHistory().add(oneJob);

        EducationEntity oneEd = new EducationEntity();
        oneEd.setEducationId(UUID.nameUUIDFromBytes("oneEd".getBytes()));
        oneEd.setEducationType(new EducationTypeEntity("graduate"));
        oneEd.setProgram("prog");
        oneEd.setStartDate(LocalDate.of(2010, 2, 3));
        oneEd.setGraduationPeriod(null);
        oneEd.setInstitutionName("My School");
        oneEd.getTechsAndSkills().add(pythonRelation);

        ProjectEntity oneEdProject = new ProjectEntity();
        oneEdProject.setProjectId(UUID.nameUUIDFromBytes("oneEdProject".getBytes()));
        oneEdProject.setProjectName("school project");
        oneEdProject.setProjectDescription("My school project");
        oneEdProject.setProjectStart(LocalDate.of(2010, 2, 4));
        oneEdProject.setProjectEnd(LocalDate.of(2011, 4, 10));
        oneEd.getProjects().add(oneEdProject);
        ProjectEntity oneEdProject2 = new ProjectEntity();
        oneEdProject2.setProjectId(UUID.nameUUIDFromBytes("oneEdProject2".getBytes()));
        oneEdProject2.setProjectName("schoolProj2");
        oneEdProject2.setProjectDescription("less cool school project");
        oneEdProject2.setProjectStart(LocalDate.of(2006, 11, 4));
        oneEdProject2.setProjectEnd(null);
        oneEd.getProjects().add(oneEdProject2);

        one.getEducationHistory().add(oneEd);
        ProjectEntity oneProject = new ProjectEntity();
        oneProject.setProjectId(UUID.nameUUIDFromBytes("oneProject".getBytes()));
        oneProject.setProjectName("hobby project");
        oneProject.setProjectDescription("My hobby project");
        oneProject.setProjectStart(LocalDate.of(2020, 2, 4));
        oneProject.setProjectEnd(LocalDate.of(2021, 3, 3));
        one.getProjects().add(oneProject);

        Mockito.when(workerRepository.findById(one.getWorkerId())).thenReturn(Optional.of(one));

        testObject.updateWorkerSkillHistory(one.getWorkerId());

        WorkerEntity afterUpdate = workerRepository.findById(one.getWorkerId()).get();
        Assertions.assertEquals(2, afterUpdate.getTechsAndSkillsEstimates().size());
        WorkerTechSkillEstimateEntity workerPythonEstimate = afterUpdate.getTechsAndSkillsEstimates().stream()
                .filter(e -> e.getTechSkill().getTechSkillId().equals(pythonSkill.getTechSkillId())).findAny().get();
        WorkerTechSkillEstimateEntity workerJavaEstimate = afterUpdate.getTechsAndSkillsEstimates().stream()
                .filter(e -> e.getTechSkill().getTechSkillId().equals(javaSkill.getTechSkillId())).findAny().get();

        Assertions.assertTrue(workerPythonEstimate.getSkillDaysEstimate() > 0);
        Assertions.assertTrue(workerJavaEstimate.getSkillDaysEstimate() > 0);
    }
}

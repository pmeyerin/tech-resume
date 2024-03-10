package coop.stlma.tech.resume.worker.service;

import coop.stlma.tech.resume.education.Education;
import coop.stlma.tech.resume.education.data.EducationType;
import coop.stlma.tech.resume.education.data.entity.EducationEntity;
import coop.stlma.tech.resume.education.data.entity.EducationTypeEntity;
import coop.stlma.tech.resume.employment.Employment;
import coop.stlma.tech.resume.employment.data.entity.EmploymentEntity;
import coop.stlma.tech.resume.project.Project;
import coop.stlma.tech.resume.project.data.entity.ProjectEntity;
import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillEntity;
import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillRelationEntity;
import coop.stlma.tech.resume.worker.Worker;
import coop.stlma.tech.resume.worker.data.WorkerRepository;
import coop.stlma.tech.resume.worker.data.entity.WorkerEntity;
import coop.stlma.tech.resume.worker.data.entity.WorkerTechSkillEstimateEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest(classes = {WorkerService.class})
public class WorkerServiceTest {

    @Autowired
    WorkerService testObject;

    @MockBean
    WorkerRepository workerRepository;

    @Captor
    ArgumentCaptor<WorkerEntity> workerCaptor;

    @Test
    public void testAddProjectToWorker_happyPath() {
        WorkerEntity worker = new WorkerEntity();
        worker.setWorkerId(UUID.nameUUIDFromBytes("worker".getBytes()));
        Project project = Project.builder()
                .projectName("Some project")
                .projectDescription("My favorite project")
                .build();

        ProjectEntity savedProject = new ProjectEntity();
        savedProject.setProjectId(UUID.nameUUIDFromBytes("project".getBytes()));
        savedProject.setProjectName(project.getProjectName());
        savedProject.setProjectDescription(project.getProjectDescription());
        WorkerEntity postSave = new WorkerEntity();
        postSave.setWorkerId(worker.getWorkerId());
        postSave.setProjects(Collections.singletonList(savedProject));

        Mockito.when(workerRepository.findById(worker.getWorkerId())).thenReturn(Optional.of(worker));
        Mockito.when(workerRepository.save(workerCaptor.capture())).thenReturn(postSave);

        Project result = testObject.addProjectToWorker(worker.getWorkerId(), project);

        Assertions.assertEquals(savedProject.getProjectId(), result.getProjectId());
        Assertions.assertEquals(savedProject.getProjectName(), result.getProjectName());
        Assertions.assertEquals(savedProject.getProjectDescription(), result.getProjectDescription());

        WorkerEntity preSave = workerCaptor.getValue();
        Assertions.assertEquals(1, preSave.getProjects().size());
        Assertions.assertEquals(savedProject.getProjectName(), preSave.getProjects().get(0).getProjectName());
        Assertions.assertEquals(savedProject.getProjectDescription(), preSave.getProjects().get(0).getProjectDescription());

    }

    @Test
    public void testGetAll_happyPath() {
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

        WorkerTechSkillEstimateEntity estimate = new WorkerTechSkillEstimateEntity();
        estimate.setWorkerSkillEstimateId(UUID.nameUUIDFromBytes("estimate".getBytes()));
        estimate.setWorker(one.getWorkerId());
        estimate.setSkillDaysEstimate(5);
        estimate.setTechSkill(pythonSkill);
        one.getTechsAndSkillsEstimates().add(estimate);

        WorkerEntity two = new WorkerEntity();
        two.setWorkerId(UUID.nameUUIDFromBytes("two".getBytes()));
        two.setWorkerName("Another dev");
        two.setWorkerEmail("another@dev.com");
        ProjectEntity twoProject = new ProjectEntity();
        twoProject.setProjectId(UUID.nameUUIDFromBytes("twoProject".getBytes()));
        twoProject.setProjectName("hobby project 2");
        twoProject.setProjectDescription("My hobby project 2");
        twoProject.setProjectStart(LocalDate.of(2010, 2, 4));
        twoProject.setProjectEnd(LocalDate.of(2015, 3, 3));
        two.getProjects().add(twoProject);
        ProjectEntity twoProject2 = new ProjectEntity();
        twoProject2.setProjectId(UUID.nameUUIDFromBytes("twoProject2".getBytes()));
        twoProject2.setProjectName("hobbyProj2");
        twoProject2.setProjectDescription("less cool hobby project");
        twoProject2.setProjectStart(LocalDate.of(2006, 11, 4));
        twoProject2.setProjectEnd(LocalDate.of(2010, 1, 31));
        two.getProjects().add(twoProject2);

        Mockito.when(workerRepository.findAll())
                .thenReturn(List.of(one, two));

        List<Worker> result = testObject.getAllWorkers();

        Assertions.assertEquals(2, result.size());

        Assertions.assertEquals(one.getWorkerId(), result.get(0).getWorkerId());
        Assertions.assertEquals("Some guy", result.get(0).getWorkerName());
        Assertions.assertEquals("some@guy.com", result.get(0).getWorkerEmail());

        Assertions.assertEquals(two.getWorkerId(), result.get(1).getWorkerId());
        Assertions.assertEquals("Another dev", result.get(1).getWorkerName());
        Assertions.assertEquals("another@dev.com", result.get(1).getWorkerEmail());

        Employment foundEmployment = result.get(0).getEmploymentHistory().get(0);
        Assertions.assertEquals(1, result.get(0).getEmploymentHistory().size());
        Assertions.assertEquals("some job", foundEmployment.getEmploymentName());
        Assertions.assertEquals("My favorite job", foundEmployment.getEmploymentDescription());
        Assertions.assertEquals(2020, foundEmployment.getEmploymentStart().get(ChronoField.YEAR));
        Assertions.assertEquals(2, foundEmployment.getEmploymentStart().get(ChronoField.MONTH_OF_YEAR));
        Assertions.assertEquals(3, foundEmployment.getEmploymentStart().get(ChronoField.DAY_OF_MONTH));
        Assertions.assertEquals(2021, foundEmployment.getEmploymentEnd().get(ChronoField.YEAR));
        Assertions.assertEquals(3, foundEmployment.getEmploymentEnd().get(ChronoField.MONTH_OF_YEAR));
        Assertions.assertEquals(4, foundEmployment.getEmploymentEnd().get(ChronoField.DAY_OF_MONTH));
        Assertions.assertEquals(1, foundEmployment.getProjects().size());
        Assertions.assertEquals(2, foundEmployment.getTechAndSkills().size());
        Assertions.assertEquals("java", foundEmployment.getTechAndSkills().get(0).getTechSkillName());
        Assertions.assertEquals(UUID.nameUUIDFromBytes("java".getBytes()), foundEmployment.getTechAndSkills().get(0).getTechSkillId());
        Assertions.assertEquals("python", foundEmployment.getTechAndSkills().get(1).getTechSkillName());
        Assertions.assertEquals(UUID.nameUUIDFromBytes("python".getBytes()), foundEmployment.getTechAndSkills().get(1).getTechSkillId());
        Assertions.assertEquals("some project", foundEmployment.getProjects().get(0).getProjectName());
        Assertions.assertEquals("My favorite project", foundEmployment.getProjects().get(0).getProjectDescription());
        Assertions.assertEquals(2020, foundEmployment.getProjects().get(0).getProjectStart().get(ChronoField.YEAR));
        Assertions.assertEquals(2, foundEmployment.getProjects().get(0).getProjectStart().get(ChronoField.MONTH_OF_YEAR));
        Assertions.assertEquals(4, foundEmployment.getProjects().get(0).getProjectStart().get(ChronoField.DAY_OF_MONTH));
        Assertions.assertEquals(2021, foundEmployment.getProjects().get(0).getProjectEnd().get(ChronoField.YEAR));
        Assertions.assertEquals(3, foundEmployment.getProjects().get(0).getProjectEnd().get(ChronoField.MONTH_OF_YEAR));
        Assertions.assertEquals(3, foundEmployment.getProjects().get(0).getProjectEnd().get(ChronoField.DAY_OF_MONTH));
        Assertions.assertEquals(1, foundEmployment.getProjects().get(0).getTechAndSkills().size());
        Assertions.assertEquals("python", foundEmployment.getProjects().get(0).getTechAndSkills().get(0).getTechSkillName());
        Assertions.assertEquals(UUID.nameUUIDFromBytes("python".getBytes()), foundEmployment.getProjects().get(0).getTechAndSkills().get(0).getTechSkillId());

        Assertions.assertEquals(1, result.get(0).getEducationHistory().size());
        Education foundEd = result.get(0).getEducationHistory().get(0);
        Assertions.assertEquals("prog", foundEd.getProgram());
        Assertions.assertEquals("My School", foundEd.getInstitutionName());
        Assertions.assertEquals(EducationType.GRADUATE, foundEd.getEducationType());
        Assertions.assertEquals(2010, foundEd.getStartDate().get(ChronoField.YEAR));
        Assertions.assertEquals(2, foundEd.getStartDate().get(ChronoField.MONTH_OF_YEAR));
        Assertions.assertEquals(3, foundEd.getStartDate().get(ChronoField.DAY_OF_MONTH));
        Assertions.assertEquals(2011, foundEd.getGraduationPeriod().get(ChronoField.YEAR));
        Assertions.assertEquals(5, foundEd.getGraduationPeriod().get(ChronoField.MONTH_OF_YEAR));
        Assertions.assertEquals(1, foundEd.getGraduationPeriod().get(ChronoField.DAY_OF_MONTH));
        Assertions.assertEquals(1, foundEd.getTechAndSkills().size());
        Assertions.assertEquals("python", foundEd.getTechAndSkills().get(0).getTechSkillName());
        Assertions.assertEquals(UUID.nameUUIDFromBytes("python".getBytes()), foundEd.getTechAndSkills().get(0).getTechSkillId());
        Assertions.assertEquals(2, foundEd.getProjects().size());
        Assertions.assertEquals("school project", foundEd.getProjects().get(0).getProjectName());
        Assertions.assertEquals("My school project", foundEd.getProjects().get(0).getProjectDescription());
        Assertions.assertEquals(2010, foundEd.getProjects().get(0).getProjectStart().get(ChronoField.YEAR));
        Assertions.assertEquals(2, foundEd.getProjects().get(0).getProjectStart().get(ChronoField.MONTH_OF_YEAR));
        Assertions.assertEquals(4, foundEd.getProjects().get(0).getProjectStart().get(ChronoField.DAY_OF_MONTH));
        Assertions.assertEquals(2011, foundEd.getProjects().get(0).getProjectEnd().get(ChronoField.YEAR));
        Assertions.assertEquals(4, foundEd.getProjects().get(0).getProjectEnd().get(ChronoField.MONTH_OF_YEAR));
        Assertions.assertEquals(10, foundEd.getProjects().get(0).getProjectEnd().get(ChronoField.DAY_OF_MONTH));
        Assertions.assertEquals("schoolProj2", foundEd.getProjects().get(1).getProjectName());
        Assertions.assertEquals("less cool school project", foundEd.getProjects().get(1).getProjectDescription());
        Assertions.assertEquals(2006, foundEd.getProjects().get(1).getProjectStart().get(ChronoField.YEAR));
        Assertions.assertEquals(11, foundEd.getProjects().get(1).getProjectStart().get(ChronoField.MONTH_OF_YEAR));
        Assertions.assertEquals(4, foundEd.getProjects().get(1).getProjectStart().get(ChronoField.DAY_OF_MONTH));
        Assertions.assertEquals(2010, foundEd.getProjects().get(1).getProjectEnd().get(ChronoField.YEAR));
        Assertions.assertEquals(1, foundEd.getProjects().get(1).getProjectEnd().get(ChronoField.MONTH_OF_YEAR));
        Assertions.assertEquals(31, foundEd.getProjects().get(1).getProjectEnd().get(ChronoField.DAY_OF_MONTH));

        Assertions.assertEquals(1, result.get(0).getHobbyProjects().size());
        Assertions.assertEquals("hobby project", result.get(0).getHobbyProjects().get(0).getProjectName());
        Assertions.assertEquals("My hobby project", result.get(0).getHobbyProjects().get(0).getProjectDescription());
        Assertions.assertEquals(2020, result.get(0).getHobbyProjects().get(0).getProjectStart().get(ChronoField.YEAR));
        Assertions.assertEquals(2, result.get(0).getHobbyProjects().get(0).getProjectStart().get(ChronoField.MONTH_OF_YEAR));
        Assertions.assertEquals(4, result.get(0).getHobbyProjects().get(0).getProjectStart().get(ChronoField.DAY_OF_MONTH));
        Assertions.assertEquals(2021, result.get(0).getHobbyProjects().get(0).getProjectEnd().get(ChronoField.YEAR));
        Assertions.assertEquals(3, result.get(0).getHobbyProjects().get(0).getProjectEnd().get(ChronoField.MONTH_OF_YEAR));
        Assertions.assertEquals(3, result.get(0).getHobbyProjects().get(0).getProjectEnd().get(ChronoField.DAY_OF_MONTH));

        Assertions.assertEquals(1, result.get(0).getWorkerSkillEstimates().size());
        Assertions.assertEquals(5, result.get(0).getWorkerSkillEstimates().get(0).getYearsSkillsEstimate());
        Assertions.assertEquals("python", result.get(0).getWorkerSkillEstimates().get(0).getTechSkillName());

    }

    @Test
    public void testGetAll_noneFound() {
        Mockito.when(workerRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<Worker> result = testObject.getAllWorkers();

        Assertions.assertEquals(0, result.size());
    }
}

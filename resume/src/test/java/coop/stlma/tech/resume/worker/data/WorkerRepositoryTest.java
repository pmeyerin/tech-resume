package coop.stlma.tech.resume.worker.data;

import coop.stlma.tech.resume.education.data.EducationTypeRepository;
import coop.stlma.tech.resume.education.data.entity.EducationEntity;
import coop.stlma.tech.resume.employment.data.EmploymentRepository;
import coop.stlma.tech.resume.employment.data.entity.EmploymentEntity;
import coop.stlma.tech.resume.project.data.ProjectRepository;
import coop.stlma.tech.resume.project.data.entity.ProjectEntity;
import coop.stlma.tech.resume.techandskill.data.TechSkillsRepository;
import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillEntity;
import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillRelationEntity;
import coop.stlma.tech.resume.worker.data.entity.WorkerEntity;
import coop.stlma.tech.resume.worker.data.entity.WorkerTechSkillEstimateEntity;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class WorkerRepositoryTest {

    @Autowired
    WorkerRepository testObject;

    @Autowired
    EducationTypeRepository educationTypeRepository;

    @Autowired
    TechSkillsRepository techSkillsRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    EmploymentRepository employmentRepository;

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("user")
            .withUsername("postgres")
            .withPassword("password");

    @AfterEach
    public void cleanup() {
        testObject.deleteAll();
        techSkillsRepository.deleteAll();
        educationTypeRepository.deleteAll();
        projectRepository.deleteAll();
        employmentRepository.deleteAll();
    }

    @Test
    public void testGetAllWorkers_noEmploymentHistory() {
        WorkerEntity saveMe = new WorkerEntity();
        saveMe.setWorkerName("Some guy");
        saveMe.setWorkerEmail("some@guy.com");
        testObject.save(saveMe);

        Iterable<WorkerEntity> found = testObject.findAll();

        List<WorkerEntity> easierToWorkWith = new ArrayList<>();
        found.forEach(easierToWorkWith::add);

        Assertions.assertEquals(1, easierToWorkWith.size());
        WorkerEntity foundWorker = easierToWorkWith.get(0);
        Assertions.assertEquals("Some guy", foundWorker.getWorkerName());
        Assertions.assertEquals("some@guy.com", foundWorker.getWorkerEmail());
        Assertions.assertNotNull(foundWorker.getWorkerId());
        Assertions.assertFalse(foundWorker.getWorkerId().toString().isEmpty());

        Assertions.assertEquals(0, foundWorker.getEmploymentHistory().size());
    }

    @Test
    public void testGetAllWorkers_OneFound() {
        TechAndSkillEntity javaSkill = new TechAndSkillEntity();
        javaSkill.setTechSkillName("java");
        javaSkill = techSkillsRepository.save(javaSkill);
        TechAndSkillEntity pythonSkill = new TechAndSkillEntity();
        pythonSkill.setTechSkillName("python");
        pythonSkill = techSkillsRepository.save(pythonSkill);
        TechAndSkillEntity rubySkill = new TechAndSkillEntity();
        rubySkill.setTechSkillName("ruby");
        rubySkill = techSkillsRepository.save(rubySkill);
        WorkerEntity saveMe = new WorkerEntity();
        saveMe.setWorkerName("Some guy");
        saveMe.setWorkerEmail("some@guy.com");
        EmploymentEntity job = new EmploymentEntity();
        job.setEmploymentName("starter");
        job.setEmploymentDescription("starter desc");
        job.setEmploymentStart(LocalDate.of(2022,11,25));
        job.setEmploymentEnd(LocalDate.of(2023,11,25));
        ProjectEntity jobProject = new ProjectEntity();
        jobProject.setProjectName("jobProject");
        jobProject.setProjectDescription("jobProject desc");
        jobProject.setProjectStart(LocalDate.of(2023,1,1));
        jobProject.setProjectEnd(LocalDate.of(2023,3,3));
        jobProject.setProjectRelation(job.getEmploymentId());
        jobProject.setProjectRelationType(EmploymentEntity.EMPLOYMENT_RELATION_TYPE);
        TechAndSkillRelationEntity projJavaRelation = new TechAndSkillRelationEntity();
        projJavaRelation.setRelationType(ProjectEntity.PROJECT_RELATION_TYPE);
        projJavaRelation.setTechSkill(javaSkill);
        jobProject.getTechsAndSkills().add(projJavaRelation);
        job.getProjects().add(jobProject);
        TechAndSkillRelationEntity jobJavaRelation = new TechAndSkillRelationEntity();
        jobJavaRelation.setRelationType(EmploymentEntity.EMPLOYMENT_RELATION_TYPE);
        jobJavaRelation.setTechSkill(javaSkill);
        TechAndSkillRelationEntity jobPythonRelation = new TechAndSkillRelationEntity();
        jobPythonRelation.setRelationType(EmploymentEntity.EMPLOYMENT_RELATION_TYPE);
        jobPythonRelation.setTechSkill(pythonSkill);
        job.getTechsAndSkills().addAll(List.of(jobJavaRelation, jobPythonRelation));
        saveMe.setEmploymentHistory(new ArrayList<>());
        saveMe.getEmploymentHistory().add(job);
        WorkerTechSkillEstimateEntity estimate = new WorkerTechSkillEstimateEntity();
        estimate.setWorker(saveMe.getWorkerId());
        estimate.setTechSkill(javaSkill);
        estimate.setSkillDaysEstimate(20);
        WorkerTechSkillEstimateEntity estimate2 = new WorkerTechSkillEstimateEntity();
        estimate2.setWorker(saveMe.getWorkerId());
        estimate2.setTechSkill(pythonSkill);
        estimate2.setSkillDaysEstimate(10);
        saveMe.getTechsAndSkillsEstimates().addAll(List.of(estimate, estimate2));

        saveMe.setEducationHistory(new ArrayList<>());
        EducationEntity education = new EducationEntity();
        education.setEducationType(educationTypeRepository.findByEducationType("graduate"));
        education.setProgram("learned stuff");
        education.setInstitutionName("School College University");
        education.setGraduationPeriod(LocalDate.of(2010, 5, 1));
        ProjectEntity educationProject = new ProjectEntity();
        educationProject.setProjectName("educationProject");
        educationProject.setProjectDescription("educationProject desc");
        educationProject.setProjectStart(LocalDate.of(2010,1,1));
        educationProject.setProjectEnd(LocalDate.of(2010,3,3));
        educationProject.setProjectRelation(education.getEducationId());
        educationProject.setProjectRelationType(EducationEntity.EDUCATION_RELATION_TYPE);
        TechAndSkillRelationEntity edRubyRelation = new TechAndSkillRelationEntity();
        edRubyRelation.setRelationType(ProjectEntity.PROJECT_RELATION_TYPE);
        edRubyRelation.setTechSkill(rubySkill);
        educationProject.getTechsAndSkills().add(edRubyRelation);
        education.getProjects().add(educationProject);
        saveMe.getEducationHistory().add(education);

        ProjectEntity hobbyProject = new ProjectEntity();
        hobbyProject.setProjectName("hobbyProject");
        hobbyProject.setProjectDescription("hobbyProject desc");
        hobbyProject.setProjectStart(LocalDate.of(2015,1,1));
        hobbyProject.setProjectEnd(LocalDate.of(2015,3,3));
        hobbyProject.setProjectRelation(saveMe.getWorkerId());
        hobbyProject.setProjectRelationType(WorkerEntity.WORKER_PROJECT_TYPE);
        TechAndSkillRelationEntity pythonRelation = new TechAndSkillRelationEntity();
        pythonRelation.setRelationType(ProjectEntity.PROJECT_RELATION_TYPE);
        pythonRelation.setTechSkill(pythonSkill);
        hobbyProject.getTechsAndSkills().add(pythonRelation);
        saveMe.getProjects().add(hobbyProject);
        testObject.save(saveMe);

        Iterable<WorkerEntity> found = testObject.findAll();

        List<WorkerEntity> easierToWorkWith = new ArrayList<>();
        found.forEach(easierToWorkWith::add);

        Assertions.assertEquals(1, easierToWorkWith.size());
        WorkerEntity foundWorker = easierToWorkWith.get(0);
        Assertions.assertEquals("Some guy", foundWorker.getWorkerName());
        Assertions.assertEquals("some@guy.com", foundWorker.getWorkerEmail());
        Assertions.assertNotNull(foundWorker.getWorkerId());
        Assertions.assertFalse(foundWorker.getWorkerId().toString().isEmpty());
        Assertions.assertEquals(1, foundWorker.getProjects().size());
        ProjectEntity foundProject = foundWorker.getProjects().get(0);
        Assertions.assertEquals("hobbyProject", foundProject.getProjectName());
        Assertions.assertEquals("hobbyProject desc", foundProject.getProjectDescription());
        Assertions.assertEquals(2015, foundProject.getProjectStart().get(ChronoField.YEAR));
        Assertions.assertEquals(1, foundProject.getProjectStart().get(ChronoField.MONTH_OF_YEAR));
        Assertions.assertEquals(1, foundProject.getProjectStart().get(ChronoField.DAY_OF_MONTH));
        Assertions.assertEquals(2015, foundProject.getProjectEnd().get(ChronoField.YEAR));
        Assertions.assertEquals(3, foundProject.getProjectEnd().get(ChronoField.MONTH_OF_YEAR));
        Assertions.assertEquals(3, foundProject.getProjectEnd().get(ChronoField.DAY_OF_MONTH));
        Assertions.assertEquals(1, foundProject.getTechsAndSkills().size());
        Assertions.assertEquals("python", foundProject.getTechsAndSkills().get(0).getTechSkill().getTechSkillName());

        Assertions.assertEquals(1, foundWorker.getEmploymentHistory().size());
        EmploymentEntity foundJob = foundWorker.getEmploymentHistory().get(0);
        Assertions.assertEquals("starter", foundJob.getEmploymentName());
        Assertions.assertEquals("starter desc", foundJob.getEmploymentDescription());
        Assertions.assertEquals(2022, foundJob.getEmploymentStart().get(ChronoField.YEAR));
        Assertions.assertEquals(11, foundJob.getEmploymentStart().get(ChronoField.MONTH_OF_YEAR));
        Assertions.assertEquals(25, foundJob.getEmploymentStart().get(ChronoField.DAY_OF_MONTH));
        Assertions.assertEquals(2023, foundJob.getEmploymentEnd().get(ChronoField.YEAR));
        Assertions.assertEquals(11, foundJob.getEmploymentEnd().get(ChronoField.MONTH_OF_YEAR));
        Assertions.assertEquals(25, foundJob.getEmploymentEnd().get(ChronoField.DAY_OF_MONTH));
        Assertions.assertEquals(2, foundJob.getTechsAndSkills().size());
        Assertions.assertTrue(foundJob.getTechsAndSkills().stream()
                .map(TechAndSkillRelationEntity::getTechSkill)
                .allMatch(skill -> skill.getTechSkillName().equals("python") || skill.getTechSkillName().equals("java")));
        Assertions.assertEquals(1, foundJob.getProjects().size());
        ProjectEntity foundJobProject = foundJob.getProjects().get(0);
        Assertions.assertEquals("jobProject", foundJobProject.getProjectName());
        Assertions.assertEquals("jobProject desc", foundJobProject.getProjectDescription());
        Assertions.assertEquals(2023, foundJobProject.getProjectStart().get(ChronoField.YEAR));
        Assertions.assertEquals(1, foundJobProject.getProjectStart().get(ChronoField.MONTH_OF_YEAR));
        Assertions.assertEquals(1, foundJobProject.getProjectStart().get(ChronoField.DAY_OF_MONTH));
        Assertions.assertEquals(2023, foundJobProject.getProjectEnd().get(ChronoField.YEAR));
        Assertions.assertEquals(3, foundJobProject.getProjectEnd().get(ChronoField.MONTH_OF_YEAR));
        Assertions.assertEquals(3, foundJobProject.getProjectEnd().get(ChronoField.DAY_OF_MONTH));
        Assertions.assertEquals(1, foundJobProject.getTechsAndSkills().size());
        Assertions.assertEquals("java", foundJobProject.getTechsAndSkills().get(0).getTechSkill().getTechSkillName());

        Assertions.assertEquals(1, foundWorker.getEducationHistory().size());
        EducationEntity foundEd = foundWorker.getEducationHistory().get(0);
        Assertions.assertEquals("graduate", foundEd.getEducationType().getEducationType());
        Assertions.assertEquals("learned stuff", foundEd.getProgram());
        Assertions.assertEquals("School College University", foundEd.getInstitutionName());
        Assertions.assertEquals(2010, foundEd.getGraduationPeriod().get(ChronoField.YEAR));
        Assertions.assertEquals(5, foundEd.getGraduationPeriod().get(ChronoField.MONTH_OF_YEAR));
        Assertions.assertEquals(1, foundEd.getGraduationPeriod().get(ChronoField.DAY_OF_MONTH));
        Assertions.assertEquals(0, foundEd.getTechsAndSkills().size());
        Assertions.assertEquals(1, foundEd.getProjects().size());
        ProjectEntity foundEdProject = foundEd.getProjects().get(0);
        Assertions.assertEquals("educationProject", foundEdProject.getProjectName());
        Assertions.assertEquals("educationProject desc", foundEdProject.getProjectDescription());
        Assertions.assertEquals(2010, foundEdProject.getProjectStart().get(ChronoField.YEAR));
        Assertions.assertEquals(1, foundEdProject.getProjectStart().get(ChronoField.MONTH_OF_YEAR));
        Assertions.assertEquals(1, foundEdProject.getProjectStart().get(ChronoField.DAY_OF_MONTH));
        Assertions.assertEquals(2010, foundEdProject.getProjectEnd().get(ChronoField.YEAR));
        Assertions.assertEquals(3, foundEdProject.getProjectEnd().get(ChronoField.MONTH_OF_YEAR));
        Assertions.assertEquals(3, foundEdProject.getProjectEnd().get(ChronoField.DAY_OF_MONTH));
        Assertions.assertEquals(1, foundEdProject.getTechsAndSkills().size());
        Assertions.assertEquals("ruby", foundEdProject.getTechsAndSkills().get(0).getTechSkill().getTechSkillName());

        Assertions.assertEquals(2, foundWorker.getTechsAndSkillsEstimates().size());
        Assertions.assertEquals("java", foundWorker.getTechsAndSkillsEstimates().get(0).getTechSkill().getTechSkillName());
        Assertions.assertEquals(20, foundWorker.getTechsAndSkillsEstimates().get(0).getSkillDaysEstimate());
        Assertions.assertEquals("python", foundWorker.getTechsAndSkillsEstimates().get(1).getTechSkill().getTechSkillName());
        Assertions.assertEquals(10, foundWorker.getTechsAndSkillsEstimates().get(1).getSkillDaysEstimate());

        //Nothing new should have been inserted
        Assertions.assertEquals(3, educationTypeRepository.count());
        Assertions.assertEquals(3, techSkillsRepository.count());
    }

    @Test
    public void testGetAllWorkers_ManyFound() {
        WorkerEntity saveMe = new WorkerEntity();
        saveMe.setWorkerName("Some guy");
        saveMe.setWorkerEmail("some@guy.com");
        testObject.save(saveMe);

        saveMe = new WorkerEntity();
        saveMe.setWorkerName("Another dev");
        saveMe.setWorkerEmail("another@dev.com");
        testObject.save(saveMe);

        saveMe = new WorkerEntity();
        saveMe.setWorkerName("Theres Three");
        saveMe.setWorkerEmail("theres@three.com");
        testObject.save(saveMe);

        Iterable<WorkerEntity> found = testObject.findAll();

        List<WorkerEntity> easierToWorkWith = new ArrayList<>();
        found.forEach(easierToWorkWith::add);

        Assertions.assertEquals(3, easierToWorkWith.size());
    }

    @Test
    public void testGetAll_noneFound() {
        Iterable<WorkerEntity> found = testObject.findAll();
        List<WorkerEntity> easierToWorkWith = new ArrayList<>();
        found.forEach(easierToWorkWith::add);
        Assertions.assertEquals(0, easierToWorkWith.size());
    }
}

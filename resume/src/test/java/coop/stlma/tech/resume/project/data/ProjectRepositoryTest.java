package coop.stlma.tech.resume.project.data;

import coop.stlma.tech.resume.education.data.EducationRepository;
import coop.stlma.tech.resume.education.data.EducationTypeRepository;
import coop.stlma.tech.resume.education.data.entity.EducationEntity;
import coop.stlma.tech.resume.education.data.entity.EducationTypeEntity;
import coop.stlma.tech.resume.employment.data.EmploymentRepository;
import coop.stlma.tech.resume.employment.data.entity.EmploymentEntity;
import coop.stlma.tech.resume.project.data.ProjectRepository;
import coop.stlma.tech.resume.project.data.entity.ProjectEntity;
import coop.stlma.tech.resume.techandskill.data.TechSkillsRepository;
import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillEntity;
import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillRelationEntity;
import coop.stlma.tech.resume.util.TestUtils;
import coop.stlma.tech.resume.worker.data.WorkerRepository;
import coop.stlma.tech.resume.worker.data.entity.WorkerEntity;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class ProjectRepositoryTest {

    @Autowired
    ProjectRepository testObject;

    @Autowired
    WorkerRepository workerRepository;

    @Autowired
    TechSkillsRepository techSkillsRepository;

    @Autowired
    EducationRepository educationRepository;

    @Autowired
    EmploymentRepository employmentRepository;

    @Autowired
    EducationTypeRepository educationTypeRepository;

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("user")
            .withUsername("postgres")
            .withPassword("password");

    @AfterEach
    public void cleanup() {
        testObject.deleteAll();
        workerRepository.deleteAll();
        techSkillsRepository.deleteAll();
        educationRepository.deleteAll();
        employmentRepository.deleteAll();
        educationTypeRepository.deleteAll();
    }

    @Test
    void testGetByWorkerAndSkill_includeEdAndEmp() {
        TechAndSkillEntity sought = new TechAndSkillEntity();
        sought.setTechSkillName("Java");
        sought = techSkillsRepository.save(sought);

        EducationTypeEntity highSchool = new EducationTypeEntity("High School");
        highSchool = educationTypeRepository.save(highSchool);

        WorkerEntity saveMe = new WorkerEntity();
        saveMe.setWorkerName("Some guy");
        saveMe.setWorkerEmail("some@guy.com");

        EducationEntity edu = new EducationEntity();
        edu.setEducationType(highSchool);
        edu.setInstitutionName("Java High");

        ProjectEntity eduProj = new ProjectEntity();
        eduProj.setProjectRelationType(EducationEntity.EDUCATION_RELATION_TYPE);
        eduProj.setTechsAndSkills(TestUtils.makeRelations(ProjectEntity.PROJECT_RELATION_TYPE, null, sought));

        edu.setProjects(List.of(eduProj));

        saveMe.setEducationHistory(List.of(edu));

        EmploymentEntity emp = new EmploymentEntity();
        emp.setWorker(saveMe.getWorkerId());
        emp.setEmploymentName("Some job");

        ProjectEntity empProj = new ProjectEntity();
        empProj.setProjectRelationType(EmploymentEntity.EMPLOYMENT_RELATION_TYPE);
        empProj.setTechsAndSkills(TestUtils.makeRelations(ProjectEntity.PROJECT_RELATION_TYPE, null, sought));

        emp.setProjects(List.of(empProj));

        saveMe.setEmploymentHistory(List.of(emp));

        saveMe = workerRepository.save(saveMe);

        List<ProjectEntity> result = testObject.findByWorkerAndSkill(saveMe.getWorkerId(), sought.getTechSkillId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void testGetByWorkerAndSkill_happyPath() {
        WorkerEntity saveMe = new WorkerEntity();
        saveMe.setWorkerName("Some guy");
        saveMe.setWorkerEmail("some@guy.com");
        saveMe = workerRepository.save(saveMe);

        TechAndSkillEntity skill = new TechAndSkillEntity();
        skill.setTechSkillName("Java");
        skill = techSkillsRepository.save(skill);

        ProjectEntity expected = new ProjectEntity();
        TechAndSkillRelationEntity relation = new TechAndSkillRelationEntity();
        relation.setRelationType(ProjectEntity.PROJECT_RELATION_TYPE);
        relation.setTechSkill(skill);
        expected.setTechsAndSkills(List.of(relation));
        expected.setProjectName("Expected");
        expected.setProjectRelation(saveMe.getWorkerId());
        expected.setProjectRelationType(WorkerEntity.WORKER_PROJECT_TYPE);
        expected = testObject.save(expected);

        ProjectEntity incorrect = new ProjectEntity();
        incorrect.setProjectName("Incorrect");
        TechAndSkillEntity incorrectSkill = new TechAndSkillEntity();
        incorrectSkill.setTechSkillName("Ruby");
        incorrectSkill = techSkillsRepository.save(incorrectSkill);
        TechAndSkillRelationEntity incorrectRelation = new TechAndSkillRelationEntity();
        incorrectRelation.setRelationType(ProjectEntity.PROJECT_RELATION_TYPE);
        incorrectRelation.setTechSkill(incorrectSkill);
        incorrect.setTechsAndSkills(List.of(incorrectRelation));
        incorrect.setProjectRelation(saveMe.getWorkerId());
        incorrect.setProjectRelationType(ProjectEntity.PROJECT_RELATION_TYPE);
        testObject.save(incorrect);

        List<ProjectEntity> found = testObject.findByWorkerAndSkill(saveMe.getWorkerId(), skill.getTechSkillId());

        Assertions.assertNotNull(found);
        Assertions.assertNotEquals(0, found.size());
        Assertions.assertEquals(expected.getProjectName(), found.get(0).getProjectName());

    }

    @Test
    void testGetByWorkerAndSkill_wrongWorker() {
        WorkerEntity saveMe = new WorkerEntity();
        saveMe.setWorkerName("Some guy");
        saveMe.setWorkerEmail("some@guy.com");
        saveMe = workerRepository.save(saveMe);

        WorkerEntity incorrect = new WorkerEntity();
        incorrect.setWorkerName("Wrong guy");
        incorrect.setWorkerEmail("wrong@guy.com");
        incorrect = workerRepository.save(incorrect);

        TechAndSkillEntity skill = new TechAndSkillEntity();
        skill.setTechSkillName("Java");
        skill = techSkillsRepository.save(skill);

        ProjectEntity expected = new ProjectEntity();
        TechAndSkillRelationEntity relation = new TechAndSkillRelationEntity();
        relation.setRelationType(ProjectEntity.PROJECT_RELATION_TYPE);
        relation.setTechSkill(skill);
        expected.setTechsAndSkills(List.of(relation));
        expected.setProjectName("Expected");
        expected.setProjectRelation(saveMe.getWorkerId());
        expected.setProjectRelationType(ProjectEntity.PROJECT_RELATION_TYPE);
        expected = testObject.save(expected);

        List<ProjectEntity> found = testObject.findByWorkerAndSkill(incorrect.getWorkerId(), skill.getTechSkillId());

        Assertions.assertEquals(0, found.size());
    }

    @Test
    void testGetByWorkerAndSkill_wrongSkill() {
        WorkerEntity saveMe = new WorkerEntity();
        saveMe.setWorkerName("Some guy");
        saveMe.setWorkerEmail("some@guy.com");
        saveMe = workerRepository.save(saveMe);

        TechAndSkillEntity skill = new TechAndSkillEntity();
        skill.setTechSkillName("Java");
        skill = techSkillsRepository.save(skill);

        TechAndSkillEntity incorrect = new TechAndSkillEntity();
        incorrect.setTechSkillName("Ruby");
        incorrect = techSkillsRepository.save(incorrect);

        ProjectEntity expected = new ProjectEntity();
        TechAndSkillRelationEntity relation = new TechAndSkillRelationEntity();
        relation.setRelationType(ProjectEntity.PROJECT_RELATION_TYPE);
        relation.setTechSkill(skill);
        expected.setTechsAndSkills(List.of(relation));
        expected.setProjectName("Expected");
        expected.setProjectRelation(saveMe.getWorkerId());
        expected.setProjectRelationType(ProjectEntity.PROJECT_RELATION_TYPE);
        expected = testObject.save(expected);

        List<ProjectEntity> found = testObject.findByWorkerAndSkill(saveMe.getWorkerId(), incorrect.getTechSkillId());

        Assertions.assertEquals(0, found.size());
    }
}

package coop.stlma.tech.resume.employment.data;

import coop.stlma.tech.resume.employment.data.EmploymentRepository;
import coop.stlma.tech.resume.employment.data.entity.EmploymentEntity;
import coop.stlma.tech.resume.techandskill.data.TechSkillsRepository;
import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillEntity;
import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillRelationEntity;
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
public class EmploymentRepositoryTest {

    @Autowired
    EmploymentRepository testObject;

    @Autowired
    WorkerRepository workerRepository;

    @Autowired
    TechSkillsRepository techSkillsRepository;

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

        EmploymentEntity expected = new EmploymentEntity();
        TechAndSkillRelationEntity relation = new TechAndSkillRelationEntity();
        relation.setRelationType(EmploymentEntity.EMPLOYMENT_RELATION_TYPE);
        relation.setTechSkill(skill);
        expected.setTechsAndSkills(List.of(relation));
        expected.setEmploymentName("Expected");
        expected.setWorker(saveMe.getWorkerId());
        expected = testObject.save(expected);

        EmploymentEntity incorrect = new EmploymentEntity();
        incorrect.setEmploymentName("Incorrect");
        TechAndSkillEntity incorrectSkill = new TechAndSkillEntity();
        incorrectSkill.setTechSkillName("Ruby");
        incorrectSkill = techSkillsRepository.save(incorrectSkill);
        TechAndSkillRelationEntity incorrectRelation = new TechAndSkillRelationEntity();
        incorrectRelation.setRelationType(EmploymentEntity.EMPLOYMENT_RELATION_TYPE);
        incorrectRelation.setTechSkill(incorrectSkill);
        incorrect.setTechsAndSkills(List.of(incorrectRelation));
        incorrect.setWorker(saveMe.getWorkerId());
        testObject.save(incorrect);

        List<EmploymentEntity> found = testObject.findByWorkerAndSkill(saveMe.getWorkerId(), skill.getTechSkillId());

        Assertions.assertNotNull(found);
        Assertions.assertNotEquals(0, found.size());
        Assertions.assertEquals(expected.getEmploymentName(), found.get(0).getEmploymentName());

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

        EmploymentEntity expected = new EmploymentEntity();
        TechAndSkillRelationEntity relation = new TechAndSkillRelationEntity();
        relation.setRelationType(EmploymentEntity.EMPLOYMENT_RELATION_TYPE);
        relation.setTechSkill(skill);
        expected.setTechsAndSkills(List.of(relation));
        expected.setEmploymentName("Expected");
        expected.setWorker(saveMe.getWorkerId());
        expected = testObject.save(expected);

        List<EmploymentEntity> found = testObject.findByWorkerAndSkill(incorrect.getWorkerId(), skill.getTechSkillId());

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

        EmploymentEntity expected = new EmploymentEntity();
        TechAndSkillRelationEntity relation = new TechAndSkillRelationEntity();
        relation.setRelationType(EmploymentEntity.EMPLOYMENT_RELATION_TYPE);
        relation.setTechSkill(skill);
        expected.setTechsAndSkills(List.of(relation));
        expected.setEmploymentName("Expected");
        expected.setWorker(saveMe.getWorkerId());
        expected = testObject.save(expected);

        List<EmploymentEntity> found = testObject.findByWorkerAndSkill(saveMe.getWorkerId(), incorrect.getTechSkillId());

        Assertions.assertEquals(0, found.size());
    }
}

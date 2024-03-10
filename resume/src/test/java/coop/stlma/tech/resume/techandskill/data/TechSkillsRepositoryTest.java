package coop.stlma.tech.resume.techandskill.data;

import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillEntity;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class TechSkillsRepositoryTest {

    @Autowired
    TechSkillsRepository testObject;

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("user")
            .withUsername("postgres")
            .withPassword("password");

    @AfterEach
    public void cleanup() {
        testObject.deleteAll();
    }

    @Test
    public void testGetByNameIn_happyPath() {

        TechAndSkillEntity saveMe = new TechAndSkillEntity();
        saveMe.setTechSkillName("Some skill");
        testObject.save(saveMe);
        saveMe = new TechAndSkillEntity();
        saveMe.setTechSkillName("Another skill");
        testObject.save(saveMe);
        saveMe = new TechAndSkillEntity();
        saveMe.setTechSkillName("wrong skill");
        testObject.save(saveMe);

        List<TechAndSkillEntity> found = testObject.findByTechSkillNameIn(List.of("Some skill", "Another skill"));

        Assertions.assertEquals(2, found.size());
        found.sort(Comparator.comparing(TechAndSkillEntity::getTechSkillName));
        Assertions.assertEquals("Another skill", found.get(0).getTechSkillName());
        Assertions.assertEquals("Some skill", found.get(1).getTechSkillName());
    }


    @Test
    public void testGetByName_found() {
        TechAndSkillEntity saveMe = new TechAndSkillEntity();
        saveMe.setTechSkillName("Some skill");
        testObject.save(saveMe);
        saveMe = new TechAndSkillEntity();
        saveMe.setTechSkillName("wrong skill");
        testObject.save(saveMe);

        Optional<TechAndSkillEntity> found = testObject.findByTechSkillName("Some skill");

        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("Some skill", found.get().getTechSkillName());
    }

    @Test
    public void testGetByName_notFound() {
        TechAndSkillEntity saveMe = new TechAndSkillEntity();
        saveMe.setTechSkillName("Some skill");
        Optional<TechAndSkillEntity> found = testObject.findByTechSkillName("Wrong skill");

        Assertions.assertFalse(found.isPresent());

    }

    @Test
    public void testGetAllTechSkills_oneFound() {
        TechAndSkillEntity saveMe = new TechAndSkillEntity();
        saveMe.setTechSkillName("Some skill");

        testObject.save(saveMe);

        Iterable<TechAndSkillEntity> found = testObject.findAll();
        List<TechAndSkillEntity> easierToWorkWith = new ArrayList<>();

        found.forEach(easierToWorkWith::add);

        Assertions.assertEquals(1, easierToWorkWith.size());
        Assertions.assertEquals("Some skill", easierToWorkWith.get(0).getTechSkillName());
    }

    @Test
    public void testGetAllTechSkills_manyFound() {
        TechAndSkillEntity saveMe = new TechAndSkillEntity();
        saveMe.setTechSkillName("Some skill");
        testObject.save(saveMe);
        saveMe = new TechAndSkillEntity();
        saveMe.setTechSkillName("another skill");

        testObject.save(saveMe);

        Iterable<TechAndSkillEntity> found = testObject.findAll();
        List<TechAndSkillEntity> easierToWorkWith = new ArrayList<>();

        found.forEach(easierToWorkWith::add);

        Assertions.assertEquals(2, easierToWorkWith.size());
    }

    @Test
    public void testGetAllTechSkills_noneFound() {
        Iterable<TechAndSkillEntity> found = testObject.findAll();
        List<TechAndSkillEntity> easierToWorkWith = new ArrayList<>();
        found.forEach(easierToWorkWith::add);
        Assertions.assertEquals(0, easierToWorkWith.size());
    }
}

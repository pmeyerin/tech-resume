package coop.stlma.tech.resume.techandskill.service;

import coop.stlma.tech.resume.education.data.EducationRepository;
import coop.stlma.tech.resume.education.data.entity.EducationEntity;
import coop.stlma.tech.resume.education.data.entity.EducationTypeEntity;
import coop.stlma.tech.resume.employment.data.EmploymentRepository;
import coop.stlma.tech.resume.employment.data.entity.EmploymentEntity;
import coop.stlma.tech.resume.project.Project;
import coop.stlma.tech.resume.project.data.ProjectRepository;
import coop.stlma.tech.resume.project.data.entity.ProjectEntity;
import coop.stlma.tech.resume.techandskill.SkillGrouping;
import coop.stlma.tech.resume.techandskill.TechAndSkill;
import coop.stlma.tech.resume.techandskill.data.TechSkillsRepository;
import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillEntity;
import coop.stlma.tech.resume.util.TestUtils;
import coop.stlma.tech.resume.worker.data.entity.WorkerEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest(classes = {TechSkillsService.class})
public class TechSkillsServiceTest {

    @Autowired
    TechSkillsService testObject;

    @MockBean
    TechSkillsRepository techSkillsRepository;
    @MockBean
    ProjectRepository projectRepository;
    @MockBean
    EducationRepository educationRepository;
    @MockBean
    EmploymentRepository employmentRepository;

    @Test
    public void testGetByWorkerAndSkill_happyPath() {
        UUID skillId = UUID.nameUUIDFromBytes("skill".getBytes());
        UUID workerId = UUID.nameUUIDFromBytes("worker".getBytes());

        ProjectEntity one = new ProjectEntity();
        one.setProjectId(UUID.nameUUIDFromBytes("one".getBytes()));
        one.setProjectName("one");
        one.setProjectDescription("oneDesc");
        one.setProjectStart(LocalDate.of(2023, 4, 5));
        one.setProjectEnd(LocalDate.of(2023, 4, 6));
        one.setProjectRelation(workerId);
        one.setProjectRelationType(WorkerEntity.WORKER_PROJECT_TYPE);
        one.setTechsAndSkills(TestUtils.makeRelations(ProjectEntity.PROJECT_RELATION_TYPE, one.getProjectId(), "java", "javascript"));

        ProjectEntity two = new ProjectEntity();
        two.setProjectId(UUID.nameUUIDFromBytes("two".getBytes()));
        two.setProjectName("two");
        two.setProjectDescription("twoDesc");
        two.setProjectStart(LocalDate.of(2023, 10, 2));
        two.setProjectEnd(LocalDate.of(2023, 10, 3));
        two.setProjectRelation(workerId);
        two.setProjectRelationType(WorkerEntity.WORKER_PROJECT_TYPE);
        two.setTechsAndSkills(TestUtils.makeRelations(ProjectEntity.PROJECT_RELATION_TYPE, two.getProjectId(), "C++", "javascript", "Ubuntu"));

        Mockito.when(projectRepository.findByWorkerAndSkill(workerId, skillId))
                .thenReturn(List.of(one, two));

        EmploymentEntity oneEmployment = new EmploymentEntity();
        oneEmployment.setEmploymentId(UUID.nameUUIDFromBytes("oneEmp".getBytes()));
        oneEmployment.setEmploymentName("one");
        oneEmployment.setEmploymentDescription("oneDesc");
        oneEmployment.setEmploymentStart(LocalDate.of(2023, 4, 5));
        oneEmployment.setEmploymentEnd(LocalDate.of(2023, 4, 6));
        oneEmployment.setTechsAndSkills(TestUtils.makeRelations(EmploymentEntity.EMPLOYMENT_RELATION_TYPE,
                oneEmployment.getEmploymentId(), "java", "javascript"));

        EducationEntity oneEd = new EducationEntity();
        oneEd.setEducationId(UUID.nameUUIDFromBytes("oneEd".getBytes()));
        oneEd.setEducationType(new EducationTypeEntity("UNDERGRADUATE"));
        oneEd.setInstitutionName("one");
        oneEd.setProgram("oneProg");
        oneEd.setStartDate(LocalDate.of(2012, 4, 5));
        oneEd.setGraduationPeriod(LocalDate.of(2016, 4, 6));
        oneEd.setTechsAndSkills(TestUtils.makeRelations(EducationEntity.EDUCATION_RELATION_TYPE,
                oneEd.getEducationId(), "java", "Grails"));

        Mockito.when(employmentRepository.findByWorkerAndSkill(workerId, skillId)).thenReturn(List.of(oneEmployment));
        Mockito.when(educationRepository.findByWorkerAndSkill(workerId, skillId)).thenReturn(List.of(oneEd));
        Mockito.when(projectRepository.findByWorkerAndSkill(workerId, skillId)).thenReturn(List.of(one, two));

        SkillGrouping result = testObject.getByWorkerAndSkill(workerId, skillId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.getProjects().size());
        Assertions.assertEquals(1, result.getEmployments().size());
        Assertions.assertEquals(1, result.getEducations().size());

        List<Project> ordered = result.getProjects().stream().sorted(Comparator.comparing(Project::getProjectName)).toList();
        Assertions.assertEquals("one", ordered.get(0).getProjectName());
        Assertions.assertEquals("oneDesc", ordered.get(0).getProjectDescription());
        Assertions.assertEquals(2023, ordered.get(0).getProjectStart().getYear());
        Assertions.assertEquals(2023, ordered.get(0).getProjectEnd().getYear());
        Assertions.assertEquals(2, ordered.get(0).getTechAndSkills().size());
        Assertions.assertEquals("two", ordered.get(1).getProjectName());
        Assertions.assertEquals("twoDesc", ordered.get(1).getProjectDescription());
        Assertions.assertEquals(2023, ordered.get(1).getProjectStart().getYear());
        Assertions.assertEquals(2023, ordered.get(1).getProjectEnd().getYear());
        Assertions.assertEquals(3, ordered.get(1).getTechAndSkills().size());

        Assertions.assertEquals("one", result.getEmployments().get(0).getEmploymentName());
        Assertions.assertEquals("oneDesc", result.getEmployments().get(0).getEmploymentDescription());
        Assertions.assertEquals(2023, result.getEmployments().get(0).getEmploymentStart().getYear());
        Assertions.assertEquals(2023, result.getEmployments().get(0).getEmploymentEnd().getYear());
        Assertions.assertEquals(2, result.getEmployments().get(0).getTechAndSkills().size());

        Assertions.assertEquals("one", result.getEducations().get(0).getInstitutionName());
        Assertions.assertEquals("oneProg", result.getEducations().get(0).getProgram());
        Assertions.assertEquals(2012, result.getEducations().get(0).getStartDate().getYear());
        Assertions.assertEquals(2016, result.getEducations().get(0).getGraduationPeriod().getYear());
        Assertions.assertEquals(2, result.getEducations().get(0).getTechAndSkills().size());

    }

    @Test
    public void testBulkSave_happyPath() {
        testObject.bulkSave(List.of("one", "two"));

        ArgumentCaptor<List<TechAndSkillEntity>> captor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(techSkillsRepository).saveAll(captor.capture());

        Assertions.assertEquals(2, captor.getValue().size());
        Assertions.assertTrue(captor.getValue().stream()
                .allMatch(entity ->
                        entity.getTechSkillName().equals("one") || entity.getTechSkillName().equals("two")));
    }

    @Test
    public void testGetAll_happyPath() {
        TechAndSkillEntity one = new TechAndSkillEntity();
        one.setTechSkillId(UUID.nameUUIDFromBytes("one".getBytes()));
        one.setTechSkillName("some skill");

        TechAndSkillEntity two = new TechAndSkillEntity();
        one.setTechSkillId(UUID.nameUUIDFromBytes("two".getBytes()));
        two.setTechSkillName("another skill");
        Mockito.when(techSkillsRepository.findAll())
                .thenReturn(List.of(one, two));

        List<TechAndSkill> result = testObject.getAllTechSkills();

        Assertions.assertEquals(2, result.size());

        Assertions.assertEquals(one.getTechSkillId(), result.get(0).getTechSkillId());
        Assertions.assertEquals(one.getTechSkillName(), result.get(0).getTechSkillName());
        Assertions.assertEquals(two.getTechSkillId(), result.get(1).getTechSkillId());
        Assertions.assertEquals(two.getTechSkillName(), result.get(1).getTechSkillName());
    }

    @Test
    public void testGetAll_noneFound() {
        List<TechAndSkill> result = testObject.getAllTechSkills();

        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void testGetByName_happyPath() {
        TechAndSkillEntity one = new TechAndSkillEntity();
        one.setTechSkillId(UUID.nameUUIDFromBytes("one".getBytes()));
        one.setTechSkillName("some skill");

        Mockito.when(techSkillsRepository.findByTechSkillName("some skill"))
                .thenReturn(Optional.of(one));

        TechAndSkill result = testObject.getByName("some skill");

        Assertions.assertEquals(one.getTechSkillId(), result.getTechSkillId());
        Assertions.assertEquals(one.getTechSkillName(), result.getTechSkillName());
    }

    @Test
    public void testGetByName_notFound() {
        Mockito.when(techSkillsRepository.findByTechSkillName("some skill"))
                .thenReturn(Optional.empty());

        TechAndSkill result = testObject.getByName("some skill");

        Assertions.assertNull(result);
    }
}

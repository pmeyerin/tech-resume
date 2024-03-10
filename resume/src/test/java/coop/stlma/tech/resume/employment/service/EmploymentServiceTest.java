package coop.stlma.tech.resume.employment.service;

import coop.stlma.tech.resume.employment.Employment;
import coop.stlma.tech.resume.employment.data.EmploymentRepository;
import coop.stlma.tech.resume.employment.data.entity.EmploymentEntity;
import coop.stlma.tech.resume.employment.error.NoSuchEmploymentException;
import coop.stlma.tech.resume.employment.service.EmploymentService;
import coop.stlma.tech.resume.project.Project;
import coop.stlma.tech.resume.project.data.entity.ProjectEntity;
import coop.stlma.tech.resume.techandskill.TechAndSkill;
import coop.stlma.tech.resume.techandskill.data.TechSkillsRepository;
import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillEntity;
import coop.stlma.tech.resume.worker.data.WorkerRepository;
import coop.stlma.tech.resume.worker.data.entity.WorkerEntity;
import coop.stlma.tech.resume.worker.error.NoSuchWorkerException;
import coop.stlma.tech.resume.worker.service.WorkerSkillUpdateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = {EmploymentService.class})
class EmploymentServiceTest {

    @Autowired
    EmploymentService testObject;

    @MockBean
    EmploymentRepository employmentRepository;
    @MockBean
    TechSkillsRepository techSkillsRepository;
    @MockBean
    WorkerSkillUpdateService workerSkillUpdateService;
    @MockBean
    WorkerRepository workerRepository;

    @Captor
    ArgumentCaptor<EmploymentEntity> employmentCaptor;

    @Test
    void testAddEmployment_happyPath() {
        UUID workerId = UUID.nameUUIDFromBytes("worker".getBytes());
        UUID employmentId = UUID.nameUUIDFromBytes("employment".getBytes());

        EmploymentEntity expected = new EmploymentEntity();
        expected.setEmploymentId(employmentId);

        Mockito.when(workerRepository.findById(workerId)).thenReturn(Optional.of(new WorkerEntity()));

        Mockito.when(employmentRepository.save(employmentCaptor.capture()))
                .thenReturn(expected);


        Employment actual = testObject.addEmployment(workerId,
                Employment.builder()
                        .employmentEnd(LocalDate.of(2023, 4, 5))
                        .employmentStart(LocalDate.of(2023, 1, 6))
                        .employmentName("Fake Job")
                        .employmentDescription("Fake Job desc")
                        .projects(List.of(
                                Project.builder()
                                        .projectName("Fake Project")
                                        .projectStart(LocalDate.of(2023, 2, 6))
                                        .projectDescription("Fake Project desc")
                                        .projectEnd(LocalDate.of(2023, 3, 6))
                                        .techAndSkills(List.of(
                                                TechAndSkill.builder()
                                                        .techSkillName("Java")
                                                        .build()
                                        ))
                                        .build()
                        ))
                        .techAndSkills(List.of(
                                TechAndSkill.builder()
                                        .techSkillName("Java")
                                        .build(),
                                TechAndSkill.builder()
                                        .techSkillName("Python")
                                        .build()
                        ))
                        .build());

        Assertions.assertEquals(employmentId, actual.getEmploymentId());

        EmploymentEntity savedEntity = employmentCaptor.getValue();
        Assertions.assertEquals(workerId, savedEntity.getWorker());
        Assertions.assertEquals("Fake Job", savedEntity.getEmploymentName());
        Assertions.assertEquals("Fake Job desc", savedEntity.getEmploymentDescription());
        Assertions.assertEquals(LocalDate.of(2023, 4, 5), savedEntity.getEmploymentEnd());
        Assertions.assertEquals(LocalDate.of(2023, 1, 6), savedEntity.getEmploymentStart());
        Assertions.assertEquals(0, savedEntity.getTechsAndSkills().size());
        Assertions.assertEquals(0, savedEntity.getProjects().size());
    }

    @Test
    void testEmployment_noWorkerFound() {
        UUID workerId = UUID.nameUUIDFromBytes("worker".getBytes());
        Assertions.assertThrows(NoSuchWorkerException.class, () -> {
            testObject.addEmployment(workerId, Employment.builder().build());
        });
    }

    @Test
    void testAddSkillsToEmployment_happyPath() {
        UUID employmentId = UUID.nameUUIDFromBytes("employment".getBytes());
        List<String> addedSkills = List.of("Java", "Python", "Javascript");

        EmploymentEntity found = new EmploymentEntity();
        found.setEmploymentId(employmentId);

        Mockito.when(employmentRepository.findById(employmentId))
                .thenReturn(Optional.of(found));

        List<TechAndSkillEntity> foundSkills = addedSkills.stream().map(s -> {
            TechAndSkillEntity tse = new TechAndSkillEntity();
            tse.setTechSkillId(UUID.nameUUIDFromBytes(s.getBytes()));
            tse.setTechSkillName(s);
            return tse;
        }).toList();

        Mockito.when(techSkillsRepository.findByTechSkillNameIn(addedSkills))
                .thenReturn(foundSkills);

        List<TechAndSkill> result = testObject.addSkillsToEmployment(employmentId, addedSkills);

        Assertions.assertEquals(3, result.size());

        Assertions.assertEquals("Java", result.get(0).getTechSkillName());
        Assertions.assertEquals("Python", result.get(1).getTechSkillName());
        Assertions.assertEquals("Javascript", result.get(2).getTechSkillName());
    }

    @Test
    void testAddSkillsToEmployment_noEmploymentFound() {
        UUID employmentId = UUID.nameUUIDFromBytes("employment".getBytes());
        Assertions.assertThrows(NoSuchEmploymentException.class, () -> {
            testObject.addSkillsToEmployment(employmentId, List.of("Java", "Python", "Javascript"));
        });
    }

    @Test
    void testAddProjectToEmployment_happyPath() {
        UUID employmentId = UUID.nameUUIDFromBytes("employment".getBytes());
        UUID projectId = UUID.nameUUIDFromBytes("project".getBytes());

        EmploymentEntity found = new EmploymentEntity();
        found.setEmploymentId(employmentId);

        EmploymentEntity afterSave = new EmploymentEntity();
        afterSave.setEmploymentId(employmentId);
        ProjectEntity project = new ProjectEntity();
        project.setProjectId(projectId);
        project.setProjectName("Fake Project");
        afterSave.setProjects(List.of(project));

        Mockito.when(employmentRepository.findById(employmentId))
                .thenReturn(Optional.of(found));

        Mockito.when(employmentRepository.save(any()))
                .thenReturn(afterSave);

        Project result = testObject.addProjectToEmployment(employmentId, Project.builder()
                .projectId(projectId)
                .projectName("Fake Project")
                .build());

        Assertions.assertEquals(projectId, result.getProjectId());

    }

    @Test
    void testAddProjectToEmployment_noEmploymentFound() {
        UUID employmentId = UUID.nameUUIDFromBytes("employment".getBytes());
        Assertions.assertThrows(NoSuchEmploymentException.class, () -> {
            testObject.addProjectToEmployment(employmentId, Project.builder().build());
        });
    }
}

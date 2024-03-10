package coop.stlma.tech.resume.education.service;

import coop.stlma.tech.resume.education.Education;
import coop.stlma.tech.resume.education.data.EducationRepository;
import coop.stlma.tech.resume.education.data.EducationType;
import coop.stlma.tech.resume.education.data.EducationTypeRepository;
import coop.stlma.tech.resume.education.data.entity.EducationEntity;
import coop.stlma.tech.resume.education.data.entity.EducationTypeEntity;
import coop.stlma.tech.resume.education.error.NoSuchEducationException;
import coop.stlma.tech.resume.education.service.EducationService;
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

@SpringBootTest(classes = {EducationService.class})
class EducationServiceTest {

    @Autowired
    EducationService testObject;

    @MockBean
    EducationRepository educationRepository;
    @MockBean
    EducationTypeRepository educationTypeRepository;
    @MockBean
    TechSkillsRepository techSkillsRepository;
    @MockBean
    WorkerSkillUpdateService workerSkillUpdateService;
    @MockBean
    WorkerRepository workerRepository;

    @Captor
    ArgumentCaptor<EducationEntity> educationCaptor;

    @Test
    void testAddEducation_happyPath() {
        UUID workerId = UUID.nameUUIDFromBytes("worker".getBytes());
        UUID educationId = UUID.nameUUIDFromBytes("education".getBytes());

        EducationEntity expected = new EducationEntity();
        expected.setEducationId(educationId);
        expected.setEducationType(new EducationTypeEntity("UNDERGRADUATE"));

        Mockito.when(workerRepository.findById(workerId)).thenReturn(Optional.of(new WorkerEntity()));

        Mockito.when(educationRepository.save(educationCaptor.capture()))
                .thenReturn(expected);

        Mockito.when(educationTypeRepository.findByEducationType(EducationType.UNDERGRADUATE.getName()))
                .thenReturn(new EducationTypeEntity("UNDERGRADUATE"));

        Education actual = testObject.addEducation(workerId,
                Education.builder()
                        .graduationPeriod(LocalDate.of(2023, 4, 5))
                        .startDate(LocalDate.of(2023, 1, 6))
                        .institutionName("Fake School")
                        .educationType(EducationType.UNDERGRADUATE)
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

        Assertions.assertEquals(educationId, actual.getEducationId());

        EducationEntity savedEntity = educationCaptor.getValue();
        Assertions.assertEquals(workerId, savedEntity.getWorker());
        Assertions.assertEquals("Fake School", savedEntity.getInstitutionName());
        Assertions.assertEquals("UNDERGRADUATE", savedEntity.getEducationType().getEducationType());
        Assertions.assertEquals(LocalDate.of(2023, 4, 5), savedEntity.getGraduationPeriod());
        Assertions.assertEquals(LocalDate.of(2023, 1, 6), savedEntity.getStartDate());
        Assertions.assertEquals(0, savedEntity.getTechsAndSkills().size());
        Assertions.assertEquals(0, savedEntity.getProjects().size());
    }

    @Test
    void testEducation_noWorkerFound() {
        UUID workerId = UUID.nameUUIDFromBytes("worker".getBytes());
        Assertions.assertThrows(NoSuchWorkerException.class, () ->
                testObject.addEducation(workerId, Education.builder().build()));
    }

    @Test
    void testAddSkillsToEducation_happyPath() {
        UUID educationId = UUID.nameUUIDFromBytes("education".getBytes());
        List<String> addedSkills = List.of("Java", "Python", "Javascript");

        EducationEntity found = new EducationEntity();
        found.setEducationId(educationId);

        Mockito.when(educationRepository.findById(educationId))
                        .thenReturn(Optional.of(found));

        List<TechAndSkillEntity> foundSkills = addedSkills.stream().map(s -> {
            TechAndSkillEntity tse = new TechAndSkillEntity();
            tse.setTechSkillId(UUID.nameUUIDFromBytes(s.getBytes()));
            tse.setTechSkillName(s);
            return tse;
        }).toList();

        Mockito.when(techSkillsRepository.findByTechSkillNameIn(addedSkills))
                        .thenReturn(foundSkills);

        List<TechAndSkill> result = testObject.addSkillsToEducation(educationId, addedSkills);

        Assertions.assertEquals(3, result.size());

        Assertions.assertEquals("Java", result.get(0).getTechSkillName());
        Assertions.assertEquals("Python", result.get(1).getTechSkillName());
        Assertions.assertEquals("Javascript", result.get(2).getTechSkillName());
    }

    @Test
    void testAddSkillsToEducation_noEducationFound() {
        UUID educationId = UUID.nameUUIDFromBytes("education".getBytes());
        Assertions.assertThrows(NoSuchEducationException.class, () -> {
            testObject.addSkillsToEducation(educationId, List.of("Java", "Python", "Javascript"));
        });
    }

    @Test
    void testAddProjectToEducation_happyPath() {
        UUID educationId = UUID.nameUUIDFromBytes("education".getBytes());
        UUID projectId = UUID.nameUUIDFromBytes("project".getBytes());

        EducationEntity found = new EducationEntity();
        found.setEducationId(educationId);

        EducationEntity afterSave = new EducationEntity();
        afterSave.setEducationId(educationId);
        ProjectEntity project = new ProjectEntity();
        project.setProjectId(projectId);
        project.setProjectName("Fake Project");
        afterSave.setProjects(List.of(project));

        Mockito.when(educationRepository.findById(educationId))
                .thenReturn(Optional.of(found));

        Mockito.when(educationRepository.save(any()))
                .thenReturn(afterSave);

        Project result = testObject.addProjectToEducation(educationId, Project.builder()
                .projectId(projectId)
                        .projectName("Fake Project")
                .build());

        Assertions.assertEquals(projectId, result.getProjectId());

    }

    @Test
    void testAddProjectToEducation_noEducationFound() {
        UUID educationId = UUID.nameUUIDFromBytes("education".getBytes());
        Assertions.assertThrows(NoSuchEducationException.class, () -> {
            testObject.addProjectToEducation(educationId, Project.builder().build());
        });
    }
}

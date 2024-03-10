package coop.stlma.tech.resume.project.service;

import coop.stlma.tech.resume.education.data.EducationRepository;
import coop.stlma.tech.resume.education.data.entity.EducationEntity;
import coop.stlma.tech.resume.employment.data.EmploymentRepository;
import coop.stlma.tech.resume.employment.data.entity.EmploymentEntity;
import coop.stlma.tech.resume.project.data.ProjectRepository;
import coop.stlma.tech.resume.project.data.entity.ProjectEntity;
import coop.stlma.tech.resume.project.error.NoSuchProjectException;
import coop.stlma.tech.resume.project.service.ProjectService;
import coop.stlma.tech.resume.techandskill.TechAndSkill;
import coop.stlma.tech.resume.techandskill.data.TechSkillsRepository;
import coop.stlma.tech.resume.techandskill.data.entity.TechAndSkillEntity;
import coop.stlma.tech.resume.util.TestUtils;
import coop.stlma.tech.resume.worker.data.entity.WorkerEntity;
import coop.stlma.tech.resume.worker.service.WorkerSkillUpdateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest(classes = {ProjectService.class})
public class ProjectServiceTest {
    @Autowired
    ProjectService testObject;

    @MockBean
    private ProjectRepository projectRepository;
    @MockBean
    private TechSkillsRepository techSkillsRepository;
    @MockBean
    private WorkerSkillUpdateService workerSkillUpdateService;
    @MockBean
    private EducationRepository educationRepository;
    @MockBean
    private EmploymentRepository employmentRepository;

    @Captor
    ArgumentCaptor<ProjectEntity> projectEntityArgumentCaptor;

    @Test
    void testAddSkillsToProject_happyPath() {
        ProjectEntity project = new ProjectEntity();
        project.setProjectId(UUID.nameUUIDFromBytes("project".getBytes()));
        project.setTechsAndSkills(TestUtils.makeRelations(ProjectEntity.PROJECT_RELATION_TYPE, project.getProjectId(),
                "java", "javascript", "C++"));
        project.setProjectRelation(UUID.nameUUIDFromBytes("worker".getBytes()));
        project.setProjectRelationType(WorkerEntity.WORKER_PROJECT_TYPE);

        TechAndSkillEntity goEntity = TestUtils.makeTechAndSkill("Go");
        TechAndSkillEntity rubyEntity = TestUtils.makeTechAndSkill("Ruby");

        Mockito.when(projectRepository.findById(project.getProjectId())).thenReturn(Optional.of(project));
        Mockito.when(techSkillsRepository.findByTechSkillNameIn(List.of("Go", "Ruby")))
                .thenReturn(List.of(goEntity, rubyEntity));

        List<TechAndSkill> result = testObject.addSkillsToProject(project.getProjectId(), List.of("Go", "Ruby"));

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Go", result.get(0).getTechSkillName());
        Assertions.assertEquals("Ruby", result.get(1).getTechSkillName());

        Mockito.verify(projectRepository).save(projectEntityArgumentCaptor.capture());
        ProjectEntity savedProject = projectEntityArgumentCaptor.getValue();
        Assertions.assertEquals(5, savedProject.getTechsAndSkills().size());

        Mockito.verify(workerSkillUpdateService).updateWorkerSkillHistory(project.getProjectRelation());
    }

    @Test
    void testFindProjectWorker_workerProject() {
        ProjectEntity project = new ProjectEntity();
        project.setProjectId(UUID.nameUUIDFromBytes("project".getBytes()));
        project.setProjectRelationType(WorkerEntity.WORKER_PROJECT_TYPE);
        project.setProjectRelation(UUID.nameUUIDFromBytes("worker".getBytes()));

        Mockito.when(projectRepository.findById(project.getProjectId())).thenReturn(Optional.of(project));

        UUID result = testObject.findProjectWorker(project.getProjectId());

        Assertions.assertEquals(project.getProjectRelation(), result);
    }

    @Test
    void testFindProjectWorker_educationalProject() {
        ProjectEntity project = new ProjectEntity();
        project.setProjectId(UUID.nameUUIDFromBytes("project".getBytes()));
        project.setProjectRelationType(EducationEntity.EDUCATION_RELATION_TYPE);
        project.setProjectRelation(UUID.nameUUIDFromBytes("education".getBytes()));

        EducationEntity education = new EducationEntity();
        education.setEducationId(project.getProjectRelation());
        education.setWorker(UUID.nameUUIDFromBytes("worker".getBytes()));

        Mockito.when(projectRepository.findById(project.getProjectId())).thenReturn(Optional.of(project));
        Mockito.when(educationRepository.findById(project.getProjectRelation())).thenReturn(Optional.of(education));

        UUID result = testObject.findProjectWorker(project.getProjectId());

        Assertions.assertEquals(education.getWorker(), result);
    }

    @Test
    void testFindProjectWorker_employmentProject() {
        ProjectEntity project = new ProjectEntity();
        project.setProjectId(UUID.nameUUIDFromBytes("project".getBytes()));
        project.setProjectRelationType(EmploymentEntity.EMPLOYMENT_RELATION_TYPE);
        project.setProjectRelation(UUID.nameUUIDFromBytes("employment".getBytes()));

        EmploymentEntity employment = new EmploymentEntity();
        employment.setEmploymentId(project.getProjectRelation());
        employment.setWorker(UUID.nameUUIDFromBytes("worker".getBytes()));

        Mockito.when(projectRepository.findById(project.getProjectId())).thenReturn(Optional.of(project));
        Mockito.when(employmentRepository.findById(project.getProjectRelation())).thenReturn(Optional.of(employment));

        UUID result = testObject.findProjectWorker(project.getProjectId());

        Assertions.assertEquals(employment.getWorker(), result);
    }

    @Test
    void testFindProjectWorker_notFound() {
        ProjectEntity project = new ProjectEntity();
        project.setProjectId(UUID.nameUUIDFromBytes("project".getBytes()));

        Mockito.when(projectRepository.findById(project.getProjectId())).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchProjectException.class, () -> testObject.findProjectWorker(project.getProjectId()));
    }

    @Test
    void testFindProjectWorker_invalidType() {
        ProjectEntity project = new ProjectEntity();
        project.setProjectId(UUID.nameUUIDFromBytes("project".getBytes()));
        project.setProjectRelationType(123);
        project.setProjectRelation(UUID.nameUUIDFromBytes("employment".getBytes()));

        Mockito.when(projectRepository.findById(project.getProjectId())).thenReturn(Optional.of(project));

        Assertions.assertThrows(IllegalArgumentException.class, () -> testObject.findProjectWorker(project.getProjectId()));
    }
}

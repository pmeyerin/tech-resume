package coop.stlma.tech.resume.education.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import coop.stlma.tech.resume.education.Education;
import coop.stlma.tech.resume.education.error.NoSuchEducationException;
import coop.stlma.tech.resume.education.service.EducationService;
import coop.stlma.tech.resume.project.Project;
import coop.stlma.tech.resume.techandskill.TechAndSkill;
import coop.stlma.tech.resume.worker.error.NoSuchWorkerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebMvcTest(controllers = EducationController.class)
class EducationControllerTest {
    @MockBean
    EducationService educationService;

    TypeReference<List<Education>> workerListType = new TypeReference<>() {};

    @Autowired
    private MockMvc mockMvc;

    @Captor
    ArgumentCaptor<Education> argumentCaptor;

    @Captor
    ArgumentCaptor<Project> projectCaptor;

    @Test
    void testAddEducationToWorker_happyPath() throws Exception {
        UUID workerId = UUID.nameUUIDFromBytes("worker".getBytes());
        Mockito.when(educationService.addEducation(eq(workerId), argumentCaptor.capture()))
                .thenReturn(Education.builder().educationId(UUID.nameUUIDFromBytes("education".getBytes())).build());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/education/" + workerId)
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(
                        Map.of("educationType", "UNDERGRADUATE",
                                "graduationPeriod", "2023-04-05",
                                "startDate", "2023-01-06",
                                "institutionName", "Fake School",
                                "techAndSkills", List.of(Map.of(
                                        "techSKillId", UUID.nameUUIDFromBytes("java".getBytes()),
                                        "techSkillName", "Java"
                                ), Map.of(
                                        "techSKillId", UUID.nameUUIDFromBytes("python".getBytes()),
                                        "techSkillName", "Python"
                                )),
                                "projects", List.of(Map.of(
                                        "projectName", "Fake Project",
                                        "projectDescription", "Fake Project desc",
                                        "projectStart", "2021-02-06",
                                        "projectEnd", "2022-03-06",
                                            "techAndSkills", List.of(Map.of(
                                                    "techSKillId", UUID.nameUUIDFromBytes("Go".getBytes()),
                                                    "techSkillName", "Go"
                                            ), Map.of(
                                                    "techSKillId", UUID.nameUUIDFromBytes("javascript".getBytes()),
                                                    "techSkillName", "javascript"
                                            ))
                                ), Map.of(
                                        "projectName", "Fake Project 2",
                                        "projectDescription", "Fake Project 2 desc",
                                        "projectStart", "2021-02-06",
                                        "projectEnd", "2022-03-06",
                                            "techAndSkills", List.of(Map.of(
                                                    "techSKillId", UUID.nameUUIDFromBytes("Groovy".getBytes()),
                                                    "techSkillName", "Groovy"
                                            ), Map.of(
                                                    "techSKillId", UUID.nameUUIDFromBytes("javascript".getBytes()),
                                                    "techSkillName", "javascript"
                                            )
                                            ))
                                )))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Assertions.assertEquals(201, result.getResponse().getStatus());
        Education responseBody = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Education.class);
        Assertions.assertEquals(UUID.nameUUIDFromBytes("education".getBytes()), responseBody.getEducationId());

        Education savedEd = argumentCaptor.getValue();
        Assertions.assertEquals("UNDERGRADUATE", savedEd.getEducationType().toString());
        Assertions.assertEquals(2023, savedEd.getGraduationPeriod().getYear());
        Assertions.assertEquals(4, savedEd.getGraduationPeriod().getMonthValue());
        Assertions.assertEquals(5, savedEd.getGraduationPeriod().getDayOfMonth());
        Assertions.assertEquals(2023, savedEd.getStartDate().getYear());
        Assertions.assertEquals(1, savedEd.getStartDate().getMonthValue());
        Assertions.assertEquals(6, savedEd.getStartDate().getDayOfMonth());
        Assertions.assertEquals("Fake School", savedEd.getInstitutionName());
        Assertions.assertEquals(2, savedEd.getTechAndSkills().size());
        Assertions.assertEquals("Java", savedEd.getTechAndSkills().get(0).getTechSkillName());
        Assertions.assertEquals("Python", savedEd.getTechAndSkills().get(1).getTechSkillName());
        Assertions.assertEquals(2, savedEd.getProjects().size());
        Assertions.assertEquals("Fake Project", savedEd.getProjects().get(0).getProjectName());
        Assertions.assertEquals("Fake Project desc", savedEd.getProjects().get(0).getProjectDescription());
        Assertions.assertEquals(2021, savedEd.getProjects().get(0).getProjectStart().getYear());
        Assertions.assertEquals(2, savedEd.getProjects().get(0).getProjectStart().getMonthValue());
        Assertions.assertEquals(6, savedEd.getProjects().get(0).getProjectStart().getDayOfMonth());
        Assertions.assertEquals(2022, savedEd.getProjects().get(0).getProjectEnd().getYear());
        Assertions.assertEquals(3, savedEd.getProjects().get(0).getProjectEnd().getMonthValue());
        Assertions.assertEquals(6, savedEd.getProjects().get(0).getProjectEnd().getDayOfMonth());
        Assertions.assertEquals(2, savedEd.getProjects().get(0).getTechAndSkills().size());
        Assertions.assertEquals("Go", savedEd.getProjects().get(0).getTechAndSkills().get(0).getTechSkillName());
        Assertions.assertEquals("javascript", savedEd.getProjects().get(0).getTechAndSkills().get(1).getTechSkillName());
        Assertions.assertEquals("Fake Project 2", savedEd.getProjects().get(1).getProjectName());
        Assertions.assertEquals("Fake Project 2 desc", savedEd.getProjects().get(1).getProjectDescription());
        Assertions.assertEquals(2021, savedEd.getProjects().get(1).getProjectStart().getYear());
        Assertions.assertEquals(2, savedEd.getProjects().get(1).getProjectStart().getMonthValue());
        Assertions.assertEquals(6, savedEd.getProjects().get(1).getProjectStart().getDayOfMonth());
        Assertions.assertEquals(2022, savedEd.getProjects().get(1).getProjectEnd().getYear());
        Assertions.assertEquals(3, savedEd.getProjects().get(1).getProjectEnd().getMonthValue());
        Assertions.assertEquals(6, savedEd.getProjects().get(1).getProjectEnd().getDayOfMonth());
        Assertions.assertEquals(2, savedEd.getProjects().get(1).getTechAndSkills().size());
        Assertions.assertEquals("Groovy", savedEd.getProjects().get(1).getTechAndSkills().get(0).getTechSkillName());
        Assertions.assertEquals("javascript", savedEd.getProjects().get(1).getTechAndSkills().get(1).getTechSkillName());

    }

    @Test
    void testAddEducationToWorker_notFound() throws Exception {
        UUID workerId = UUID.nameUUIDFromBytes("worker".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/education/" + workerId)
            .contentType("application/json")
            .content(new ObjectMapper().writeValueAsString(
                Map.of("educationType", "UNDERGRADUATE",
                    "graduationPeriod", "2023-04-05",
                    "startDate", "2023-01-06",
                    "institutionName", "Fake School"))))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();

        Mockito.verify(educationService).addEducation(eq(workerId), any(Education.class));
    }

    @Test
    void testAddEducationToWorker_workerNotFound() throws Exception {
        UUID workerId = UUID.nameUUIDFromBytes("worker".getBytes());

        Mockito.when(educationService.addEducation(eq(workerId), any(Education.class)))
                .thenThrow(new NoSuchWorkerException(workerId));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/education/" + workerId)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(
                                Map.of("educationType", "UNDERGRADUATE",
                                        "graduationPeriod", "2023-04-05",
                                        "startDate", "2023-01-06",
                                        "institutionName", "Fake School"))))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        Mockito.verify(educationService).addEducation(eq(workerId), any(Education.class));
    }

    @Test
    void testAddSkillToEducation_happyPath() throws Exception {
        UUID educationId = UUID.nameUUIDFromBytes("education".getBytes());

        Mockito.when(educationService.addSkillsToEducation(eq(educationId), eq(List.of("Java", "Python", "Javascript"))))
                .thenReturn(List.of(TechAndSkill.builder()
                            .techSkillId(UUID.nameUUIDFromBytes("java".getBytes()))
                            .techSkillName("java")
                            .build(),
                        TechAndSkill.builder()
                            .techSkillId(UUID.nameUUIDFromBytes("python".getBytes()))
                            .techSkillName("python")
                            .build(),
                        TechAndSkill.builder()
                            .techSkillId(UUID.nameUUIDFromBytes("javascript".getBytes()))
                            .techSkillName("javascript")
                            .build()));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/education/techSkill/" + educationId)
                        .contentType("application/json")
                        .content("[\"Java\", \"Python\", \"Javascript\"]"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<TechAndSkill> resultBody = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<>() { });

        Assertions.assertEquals(3, resultBody.size());
        Assertions.assertEquals("java", resultBody.get(0).getTechSkillName());
        Assertions.assertEquals("python", resultBody.get(1).getTechSkillName());
        Assertions.assertEquals("javascript", resultBody.get(2).getTechSkillName());
    }

    @Test
    void testAddSkillToEducation_notFound() throws Exception {
        UUID educationId = UUID.nameUUIDFromBytes("education".getBytes());

        Mockito.when(educationService.addSkillsToEducation(eq(educationId), eq(List.of("Java", "Python", "Javascript"))))
                .thenThrow(new NoSuchEducationException(educationId));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/education/techSkill/" + educationId)
                        .contentType("application/json")
                        .content("[\"Java\", \"Python\", \"Javascript\"]"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        Mockito.verify(educationService).addSkillsToEducation(eq(educationId), eq(List.of("Java", "Python", "Javascript")));
    }

    @Test
    void testAddProjectToEducation_happyPath() throws Exception {
        UUID educationId = UUID.nameUUIDFromBytes("education".getBytes());
        UUID projectId = UUID.nameUUIDFromBytes("projectId".getBytes());
        Mockito.when(educationService.addProjectToEducation(eq(educationId), projectCaptor.capture()))
                .thenReturn(Project.builder()
                        .projectId(projectId)
                        .build());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/education/project/" + educationId)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(
                                Map.of("projectName", "Fake Project",
                                        "projectDescription", "Fake Project Desc",
                                        "projectStart", "2023-01-06",
                                        "projectEnd", "2023-03-06"
                        ))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Project resultBody = new ObjectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<>() { });

        Assertions.assertEquals(projectId, resultBody.getProjectId());
        Project captured = projectCaptor.getValue();
        Assertions.assertEquals("Fake Project", captured.getProjectName());
        Assertions.assertEquals("Fake Project Desc", captured.getProjectDescription());
        Assertions.assertEquals(LocalDate.of(2023, 1, 6), captured.getProjectStart());
        Assertions.assertEquals(LocalDate.of(2023, 3, 6), captured.getProjectEnd());
        Assertions.assertEquals(0, captured.getTechAndSkills().size());
    }

    @Test
    void testAddProjectToEducation_notFound() throws Exception {
        UUID educationId = UUID.nameUUIDFromBytes("education".getBytes());
        Mockito.when(educationService.addProjectToEducation(eq(educationId), any(Project.class)))
                .thenThrow(new NoSuchEducationException(educationId));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/education/project/" + educationId)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(
                                Map.of("projectName", "Fake Project",
                                        "projectDescription", "Fake Project Desc",
                                        "projectStart", "2023-01-06",
                                        "projectEnd", "2023-03-06"
                        ))))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        Mockito.verify(educationService).addProjectToEducation(eq(educationId), any(Project.class));
    }
}

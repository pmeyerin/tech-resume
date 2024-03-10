package coop.stlma.tech.resume.employment.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import coop.stlma.tech.resume.employment.Employment;
import coop.stlma.tech.resume.employment.controller.EmploymentController;
import coop.stlma.tech.resume.employment.error.NoSuchEmploymentException;
import coop.stlma.tech.resume.employment.service.EmploymentService;
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

@WebMvcTest(controllers = EmploymentController.class)
class EmploymentControllerTest {
    @MockBean
    EmploymentService employmentService;

    @Autowired
    private MockMvc mockMvc;

    @Captor
    ArgumentCaptor<Employment> argumentCaptor;

    @Captor
    ArgumentCaptor<Project> projectCaptor;

    @Test
    void testAddEmploymentToWorker_happyPath() throws Exception {
        UUID workerId = UUID.nameUUIDFromBytes("worker".getBytes());
        Mockito.when(employmentService.addEmployment(eq(workerId), argumentCaptor.capture()))
                .thenReturn(Employment.builder().employmentId(UUID.nameUUIDFromBytes("employment".getBytes())).build());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/employment/" + workerId)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(
                                Map.of("employmentEnd", "2023-04-05",
                                        "employmentStart", "2023-01-06",
                                        "employmentName", "Fake Job",
                                        "employmentDescription", "Fake Job desc",
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
        Employment responseBody = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Employment.class);
        Assertions.assertEquals(UUID.nameUUIDFromBytes("employment".getBytes()), responseBody.getEmploymentId());

        Employment savedEmp = argumentCaptor.getValue();
        Assertions.assertEquals("Fake Job", savedEmp.getEmploymentName());
        Assertions.assertEquals("Fake Job desc", savedEmp.getEmploymentDescription());
        Assertions.assertEquals(LocalDate.of(2023, 1, 6), savedEmp.getEmploymentStart());
        Assertions.assertEquals(LocalDate.of(2023, 4, 5), savedEmp.getEmploymentEnd());
        Assertions.assertEquals(2, savedEmp.getTechAndSkills().size());
        Assertions.assertEquals("Java", savedEmp.getTechAndSkills().get(0).getTechSkillName());
        Assertions.assertEquals("Python", savedEmp.getTechAndSkills().get(1).getTechSkillName());
        Assertions.assertEquals(2, savedEmp.getProjects().size());
        Assertions.assertEquals("Fake Project", savedEmp.getProjects().get(0).getProjectName());
        Assertions.assertEquals("Fake Project desc", savedEmp.getProjects().get(0).getProjectDescription());
        Assertions.assertEquals(2021, savedEmp.getProjects().get(0).getProjectStart().getYear());
        Assertions.assertEquals(2, savedEmp.getProjects().get(0).getProjectStart().getMonthValue());
        Assertions.assertEquals(6, savedEmp.getProjects().get(0).getProjectStart().getDayOfMonth());
        Assertions.assertEquals(2022, savedEmp.getProjects().get(0).getProjectEnd().getYear());
        Assertions.assertEquals(3, savedEmp.getProjects().get(0).getProjectEnd().getMonthValue());
        Assertions.assertEquals(6, savedEmp.getProjects().get(0).getProjectEnd().getDayOfMonth());
        Assertions.assertEquals(2, savedEmp.getProjects().get(0).getTechAndSkills().size());
        Assertions.assertEquals("Go", savedEmp.getProjects().get(0).getTechAndSkills().get(0).getTechSkillName());
        Assertions.assertEquals("javascript", savedEmp.getProjects().get(0).getTechAndSkills().get(1).getTechSkillName());
        Assertions.assertEquals("Fake Project 2", savedEmp.getProjects().get(1).getProjectName());
        Assertions.assertEquals("Fake Project 2 desc", savedEmp.getProjects().get(1).getProjectDescription());
        Assertions.assertEquals(2021, savedEmp.getProjects().get(1).getProjectStart().getYear());
        Assertions.assertEquals(2, savedEmp.getProjects().get(1).getProjectStart().getMonthValue());
        Assertions.assertEquals(6, savedEmp.getProjects().get(1).getProjectStart().getDayOfMonth());
        Assertions.assertEquals(2022, savedEmp.getProjects().get(1).getProjectEnd().getYear());
        Assertions.assertEquals(3, savedEmp.getProjects().get(1).getProjectEnd().getMonthValue());
        Assertions.assertEquals(6, savedEmp.getProjects().get(1).getProjectEnd().getDayOfMonth());
        Assertions.assertEquals(2, savedEmp.getProjects().get(1).getTechAndSkills().size());
        Assertions.assertEquals("Groovy", savedEmp.getProjects().get(1).getTechAndSkills().get(0).getTechSkillName());
        Assertions.assertEquals("javascript", savedEmp.getProjects().get(1).getTechAndSkills().get(1).getTechSkillName());

    }

    @Test
    void testAddEmploymentToWorker_notFound() throws Exception {
        UUID workerId = UUID.nameUUIDFromBytes("worker".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/employment/" + workerId)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(
                                Map.of("employmentType", "UNDERGRADUATE",
                                        "graduationPeriod", "2023-04-05",
                                        "startDate", "2023-01-06",
                                        "institutionName", "Fake School"))))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        Mockito.verify(employmentService).addEmployment(eq(workerId), any(Employment.class));
    }

    @Test
    void testAddEmploymentToWorker_workerNotFound() throws Exception {
        UUID workerId = UUID.nameUUIDFromBytes("worker".getBytes());

        Mockito.when(employmentService.addEmployment(eq(workerId), any(Employment.class)))
                .thenThrow(new NoSuchWorkerException(workerId));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/employment/" + workerId)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(
                                Map.of("employmentType", "UNDERGRADUATE",
                                        "graduationPeriod", "2023-04-05",
                                        "startDate", "2023-01-06",
                                        "institutionName", "Fake School"))))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        Mockito.verify(employmentService).addEmployment(eq(workerId), any(Employment.class));
    }

    @Test
    void testAddSkillToEmployment_happyPath() throws Exception {
        UUID employmentId = UUID.nameUUIDFromBytes("employment".getBytes());

        Mockito.when(employmentService.addSkillsToEmployment(eq(employmentId), eq(List.of("Java", "Python", "Javascript"))))
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

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/employment/techSkill/" + employmentId)
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
    void testAddSkillToEmployment_notFound() throws Exception {
        UUID employmentId = UUID.nameUUIDFromBytes("employment".getBytes());

        Mockito.when(employmentService.addSkillsToEmployment(eq(employmentId), eq(List.of("Java", "Python", "Javascript"))))
                .thenThrow(new NoSuchEmploymentException(employmentId));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/employment/techSkill/" + employmentId)
                        .contentType("application/json")
                        .content("[\"Java\", \"Python\", \"Javascript\"]"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        Mockito.verify(employmentService).addSkillsToEmployment(eq(employmentId), eq(List.of("Java", "Python", "Javascript")));
    }

    @Test
    void testAddProjectToEmployment_happyPath() throws Exception {
        UUID employmentId = UUID.nameUUIDFromBytes("employment".getBytes());
        UUID projectId = UUID.nameUUIDFromBytes("projectId".getBytes());
        Mockito.when(employmentService.addProjectToEmployment(eq(employmentId), projectCaptor.capture()))
                .thenReturn(Project.builder()
                        .projectId(projectId)
                        .build());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/employment/project/" + employmentId)
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
    void testAddProjectToEmployment_notFound() throws Exception {
        UUID employmentId = UUID.nameUUIDFromBytes("employment".getBytes());
        Mockito.when(employmentService.addProjectToEmployment(eq(employmentId), any(Project.class)))
                .thenThrow(new NoSuchEmploymentException(employmentId));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/employment/project/" + employmentId)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(
                                Map.of("projectName", "Fake Project",
                                        "projectDescription", "Fake Project Desc",
                                        "projectStart", "2023-01-06",
                                        "projectEnd", "2023-03-06"
                                ))))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        Mockito.verify(employmentService).addProjectToEmployment(eq(employmentId), any(Project.class));
    }
}

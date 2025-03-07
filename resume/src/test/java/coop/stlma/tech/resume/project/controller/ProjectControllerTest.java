package coop.stlma.tech.resume.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import coop.stlma.tech.resume.config.BasicAuthConfigAdapter;
import coop.stlma.tech.resume.project.Project;
import coop.stlma.tech.resume.project.error.InvalidProjectException;
import coop.stlma.tech.resume.project.service.ProjectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@WebMvcTest(controllers = ProjectController.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Import(BasicAuthConfigAdapter.class)
class ProjectControllerTest {

    @MockBean(ProjectService.class)
    private ProjectService projectService;

    @Autowired
    private MockMvc mockMvc;

    @Captor
    private ArgumentCaptor<Project> argumentCaptor;

    @Test
    void testUpdateProject_nullId() throws Exception {
        Mockito.when(projectService.updateProject(Mockito.any(Project.class)))
                .thenThrow(new InvalidProjectException("Field projectId is required for update"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/project")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("user", "ChangeMe"))
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(
                                Map.of("projectName", "Project One",
                                        "projectDescription", "The First Project",
                                        "projectStart", "2023-04-05",
                                        "projectEnd", "2023-05-05",
                                        "workerId", UUID.nameUUIDFromBytes("worker".getBytes())
                                ))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Field projectId is required for update"));
    }

    @Test
    void testUpdateProject_noAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/project")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(
                                Map.of(
                                        "projectId", UUID.nameUUIDFromBytes("project".getBytes()),
                                        "projectName", "Project One",
                                        "projectDescription", "The First Project",
                                        "projectStart", "2023-04-05",
                                        "projectEnd", "2023-05-05",
                                        "workerId", UUID.nameUUIDFromBytes("worker".getBytes())
                                ))))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void testUpdateProject_wrongPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/project")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("user", "WrongPassword"))
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(
                                Map.of(
                                        "projectId", UUID.nameUUIDFromBytes("project".getBytes()),
                                        "projectName", "Project One",
                                        "projectDescription", "The First Project",
                                        "projectStart", "2023-04-05",
                                        "projectEnd", "2023-05-05",
                                        "workerId", UUID.nameUUIDFromBytes("worker".getBytes())
                                ))))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void testUpdateProject_happyPath() throws Exception {
        UUID workerId = UUID.nameUUIDFromBytes("worker".getBytes());
        Mockito.when(projectService.updateProject(argumentCaptor.capture()))
                .thenReturn(Project.builder().projectId(UUID.nameUUIDFromBytes("project".getBytes())).build());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/project")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("user", "ChangeMe"))
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(
                                Map.of(
                                        "projectId", UUID.nameUUIDFromBytes("project".getBytes()),
                                        "projectName", "Project One",
                                        "projectDescription", "The First Project",
                                        "projectStart", "2023-04-05",
                                        "projectEnd", "2023-05-05",
                                        "workerId", workerId
                                ))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Project responseBody = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Project.class);
        Assertions.assertEquals(UUID.nameUUIDFromBytes("project".getBytes()), responseBody.getProjectId());

        Project saveProj = argumentCaptor.getValue();
        Assertions.assertEquals(UUID.nameUUIDFromBytes("project".getBytes()), saveProj.getProjectId());
        Assertions.assertEquals("Project One", saveProj.getProjectName());
        Assertions.assertEquals("The First Project", saveProj.getProjectDescription());
        Assertions.assertEquals(LocalDate.parse("2023-04-05"), saveProj.getProjectStart());
        Assertions.assertEquals(LocalDate.parse("2023-05-05"), saveProj.getProjectEnd());

    }
}

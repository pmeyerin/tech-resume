package coop.stlma.tech.resume.worker.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import coop.stlma.tech.resume.project.Project;
import coop.stlma.tech.resume.worker.Worker;
import coop.stlma.tech.resume.worker.service.WorkerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = WorkerController.class)
public class WorkerControllerTest {

    @MockBean
    WorkerService workerService;

    TypeReference<List<Worker>> workerListType = new TypeReference<>() {};
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .alwaysDo(print())
                .build();
    }

    @Test
    public void testAddProjectToWorker() throws Exception {
        UUID workerId = UUID.nameUUIDFromBytes("one".getBytes());

        Project newProject = Project.builder()
                .projectName("Some project")
                .projectDescription("My favorite project")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        Mockito.when(workerService.addProjectToWorker(workerId, newProject))
                .thenReturn(newProject);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/worker/project/" + workerId)
                .contentType("application/json")
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(Map.of("projectName", "Some project", "projectDescription", "My favorite project"))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        Project responseBody = objectMapper.readValue(result.getResponse().getContentAsString(), Project.class);
        Assertions.assertEquals("Some project", responseBody.getProjectName());
        Assertions.assertEquals("My favorite project", responseBody.getProjectDescription());
    }

    @Test
    public void testGetWorker() throws Exception {
        UUID workerId = UUID.nameUUIDFromBytes("one".getBytes());
        Mockito.when(workerService.getWorker(workerId))
                .thenReturn(new Worker(workerId, "One name", "111-1111",
                        "one@email.com", Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList()));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/worker/" + workerId))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        Worker responseBody = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Worker.class);
        Assertions.assertEquals("One name", responseBody.getWorkerName());
        Assertions.assertEquals("111-1111", responseBody.getWorkerPhone());
        Assertions.assertEquals("one@email.com", responseBody.getWorkerEmail());
        Assertions.assertEquals(workerId, responseBody.getWorkerId());

    }

    @Test
    public void testGetAllWorkers() throws Exception {
        Mockito.when(workerService.getAllWorkers())
                .thenReturn(List.of(
                        new Worker(UUID.nameUUIDFromBytes("one".getBytes()), "One name", "111-1111",
                                "one@email.com", Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                                Collections.emptyList())
                ));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/worker"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

        List<Worker> responseBody = new ObjectMapper().readValue(result.getResponse().getContentAsString(), workerListType);
        Assertions.assertEquals(1, responseBody.size());
        Assertions.assertEquals("One name", responseBody.get(0).getWorkerName());
        Assertions.assertEquals("111-1111", responseBody.get(0).getWorkerPhone());
        Assertions.assertEquals("one@email.com", responseBody.get(0).getWorkerEmail());
    }
}

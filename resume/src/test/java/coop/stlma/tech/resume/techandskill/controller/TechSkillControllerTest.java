package coop.stlma.tech.resume.techandskill.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import coop.stlma.tech.resume.techandskill.TechAndSkill;
import coop.stlma.tech.resume.techandskill.service.TechSkillsService;
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
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = TechSkillController.class)
public class TechSkillControllerTest {
    @MockBean
    TechSkillsService techSkillsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    TypeReference<List<TechAndSkill>> skillListType = new TypeReference<List<TechAndSkill>>() {};

    @BeforeEach
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .alwaysDo(print())
                .build();
    }

    @Test
    public void testGetAllTechSkills_happyPath() throws Exception {
        Mockito.when(techSkillsService.getAllTechSkills())
                .thenReturn(List.of(TechAndSkill.builder()
                        .techSkillId(UUID.nameUUIDFromBytes("one".getBytes()))
                        .techSkillName("one")
                        .build(),
                        TechAndSkill.builder()
                        .techSkillId(UUID.nameUUIDFromBytes("two".getBytes()))
                        .techSkillName("two")
                        .build()));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/tech-skills"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Assertions.assertEquals(200, result.getResponse().getStatus());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        List<TechAndSkill> responseBody = new ObjectMapper().readValue(result.getResponse().getContentAsString(), skillListType);

        Assertions.assertEquals(2, responseBody.size());
        Assertions.assertEquals("one", responseBody.get(0).getTechSkillName());
        Assertions.assertEquals("two", responseBody.get(1).getTechSkillName());
        Assertions.assertNotNull(responseBody.get(0).getTechSkillId());
        Assertions.assertNotNull(responseBody.get(1).getTechSkillId());
    }

    @Test
    public void testGetAllTechSKills_noneFound() throws Exception {
        Mockito.when(techSkillsService.getAllTechSkills()).thenReturn(Collections.emptyList());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/tech-skills"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Assertions.assertEquals(200, result.getResponse().getStatus());
        List<TechAndSkill> responseBody = new ObjectMapper().readValue(result.getResponse().getContentAsString(), skillListType);

        Assertions.assertEquals(0, responseBody.size());
    }

    @Test
    public void testGetByName_happyPath() throws Exception {
        Mockito.when(techSkillsService.getByName("one"))
                .thenReturn(TechAndSkill.builder()
                        .techSkillId(UUID.nameUUIDFromBytes("one".getBytes()))
                        .techSkillName("one")
                        .build());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/tech-skills/one"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Assertions.assertEquals(200, result.getResponse().getStatus());
        TechAndSkill responseBody = new ObjectMapper().readValue(result.getResponse().getContentAsString(), TechAndSkill.class);

        Assertions.assertEquals("one", responseBody.getTechSkillName());
        Assertions.assertNotNull(responseBody.getTechSkillId());
    }

    @Test
    public void testGetByName_notFound() throws Exception {
        Mockito.when(techSkillsService.getByName("one")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/tech-skill/one"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }
}

package coop.stlma.tech.resume.techandskill.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import coop.stlma.tech.resume.config.BasicAuthConfigAdapter;
import coop.stlma.tech.resume.techandskill.TechAndSkill;
import coop.stlma.tech.resume.techandskill.service.TechSkillsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = TechSkillController.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Import(BasicAuthConfigAdapter.class)
class TechSkillControllerTest {
    @MockBean
    TechSkillsService techSkillsService;

    @Autowired
    private MockMvc mockMvc;

    TypeReference<List<TechAndSkill>> skillListType = new TypeReference<List<TechAndSkill>>() {};


    @Test
    void bulkSave_noAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/tech-skills")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(Collections.singletonList(TechAndSkill.builder()
                                .techSkillId(UUID.nameUUIDFromBytes("one".getBytes()))
                                .techSkillName("one")
                                .build()))))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void bulkSave_wrongAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/tech-skills")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("user", "wrong"))
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(Collections.singletonList(TechAndSkill.builder()
                                .techSkillId(UUID.nameUUIDFromBytes("one".getBytes()))
                                .techSkillName("one")
                                .build()))))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void bulkSave_happyPath() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/tech-skills")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("user", "ChangeMe"))
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(List.of("one", "two", "three")
                        )))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Mockito.verify(techSkillsService).bulkSave(List.of("one", "two", "three"));
    }

    @Test
    void testGetAllTechSkills_happyPath() throws Exception {
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
    void testGetAllTechSKills_noneFound() throws Exception {
        Mockito.when(techSkillsService.getAllTechSkills()).thenReturn(Collections.emptyList());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/tech-skills"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Assertions.assertEquals(200, result.getResponse().getStatus());
        List<TechAndSkill> responseBody = new ObjectMapper().readValue(result.getResponse().getContentAsString(), skillListType);

        Assertions.assertEquals(0, responseBody.size());
    }

    @Test
    void testGetByName_happyPath() throws Exception {
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
    void testGetByName_notFound() throws Exception {
        Mockito.when(techSkillsService.getByName("one")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/tech-skill/one"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }
}

package fr.lernejo.search.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ElasticSearchControllerTest {

    @Test
    void should_query_be_valid(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc
            .perform(MockMvcRequestBuilders.get("/api/games").param("query", "genre:Strategy AND developer:\"Epic Games\""))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void should_query_be_invalid(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc
            .perform(MockMvcRequestBuilders.get("/api/games").param("query", "genre:"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }



}

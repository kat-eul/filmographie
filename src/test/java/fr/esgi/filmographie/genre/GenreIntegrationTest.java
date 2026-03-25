package fr.esgi.filmographie.genre;

import fr.esgi.filmographie.genre.dto.GenreDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GenreIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GenreRepository genreRepository;

    @BeforeEach
    void setUp() {
        genreRepository.deleteAll();
    }

    @Test
    void shouldCreateFindUpdateAndDeleteGenre() throws Exception {
        final var genreToCreate = GenreDTO.builder()
                .name("Comedy")
                .build();

        // CREATE
        final var createResponse = mockMvc.perform(post("/v1/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreToCreate)))
                .andExpect(status().isCreated())
                .andReturn();

        final var createdMovie = objectMapper.readValue(
                createResponse.getResponse().getContentAsString(),
                GenreDTO.class
        );

        final var genreId = createdMovie.getId();
        assertThat(genreId).isNotNull();

        // GET BY ID
        mockMvc.perform(get("/v1/genres/{genreId}", genreId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Comedy"));

        // UPDATE
        createdMovie.setName("Romance");
        mockMvc.perform(put("/v1/genres/{genreId}", genreId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdMovie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Romance"));

        // DELETE
        mockMvc.perform(delete("/v1/genres/{genreId}", genreId))
                .andExpect(status().isNoContent());

        // VERIFY DELETED
        mockMvc.perform(get("/v1/genres/{genreId}", genreId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET v1/genres should return all records in DB")
    void shouldReturnAllMovies() throws Exception {
        final var m1 = GenreEntity.builder().name("Action").build();
        final var m2 = GenreEntity.builder().name("Comedy").build();
        genreRepository.saveAll(List.of(m1, m2));

        mockMvc.perform(get("/v1/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
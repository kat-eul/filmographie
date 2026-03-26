package fr.esgi.filmographie.movie;

import fr.esgi.filmographie.genre.GenreEntity;
import fr.esgi.filmographie.genre.GenreRepository;
import fr.esgi.filmographie.movie.dto.MovieDTO;
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

import java.time.LocalDate;
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
class MovieIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @BeforeEach
    void setUp() {
        movieRepository.deleteAll();
    }

    @Test
    void shouldCreateFindUpdateAndDeleteMovie() throws Exception {
        final var SciFiGenre = GenreEntity.builder().name("Sci-Fi").build();
        final var sciFiGenreId = this.genreRepository.save(SciFiGenre).getId();

        final var movieToCreate = MovieDTO.builder()
                .title("Interstellar")
                .summary("Space exploration")
                .releaseDate(LocalDate.of(2014, 11, 7))
                .build();

        // CREATE
        final var createResponse = mockMvc.perform(post("/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieToCreate)))
                .andExpect(status().isCreated())
                .andReturn();

        final var createdMovie = objectMapper.readValue(
                createResponse.getResponse().getContentAsString(),
                MovieDTO.class
        );

        final var movieId = createdMovie.getId();
        assertThat(movieId).isNotNull();

        // GET BY ID
        mockMvc.perform(get("/v1/movies/{movieId}", movieId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Interstellar"))
                .andExpect(jsonPath("$.genres").isEmpty());

        // UPDATE
        createdMovie.setTitle("Interstellar - Director's Cut");
        mockMvc.perform(put("/v1/movies/{movieId}", movieId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdMovie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Interstellar - Director's Cut"));

        // ADD GENRE
        mockMvc.perform(post("/v1/movies/{movieId}/genres/{genreId}", movieId, sciFiGenreId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.genres").isNotEmpty())
                .andExpect(jsonPath("$.genres").isArray())
                .andExpect(jsonPath("$.genres[0].name").value("Sci-Fi"));


        // DELETE
        mockMvc.perform(delete("/v1/movies/{movieId}", movieId))
                .andExpect(status().isNoContent());

        // VERIFY DELETED
        mockMvc.perform(get("/v1/movies/{movieId}", movieId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /movies should return all records in DB")
    void shouldReturnAllMovies() throws Exception {
        final var m1 = MovieEntity.builder().title("Movie 1").build();
        final var m2 = MovieEntity.builder().title("Movie 2").build();
        movieRepository.saveAll(List.of(m1, m2));

        mockMvc.perform(get("/v1/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
package fr.esgi.filmographie.person;

import fr.esgi.filmographie.enums.JobEnum;
import fr.esgi.filmographie.person.dto.PersonDTO;
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
class PersonIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        personRepository.deleteAll();
    }

    @Test
    void shouldCreateFindUpdateAndDeletePerson_usingFirstAndLastName() throws Exception {
        final var personToCreate = PersonDTO.builder()
                .firstName("Christopher")
                .lastName("Nolan")
                .job(JobEnum.REALISATOR)
                .build();

        // CREATE
        final var createResponse = mockMvc.perform(post("/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personToCreate)))
                .andExpect(status().isCreated())
                .andReturn();

        final var createdPerson = objectMapper.readValue(
                createResponse.getResponse().getContentAsString(),
                PersonDTO.class
        );

        final var personId = createdPerson.getId();
        assertThat(personId).isNotNull();

        // GET BY ID
        mockMvc.perform(get("/v1/persons/{personId}", personId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Christopher"))
                .andExpect(jsonPath("$.lastName").value("Nolan"))
                .andExpect(jsonPath("$.job").value("REALISATOR"));

        // UPDATE
        createdPerson.setNickName("Nol");
        createdPerson.setJob(JobEnum.REALISATOR_ACTOR);
        mockMvc.perform(put("/v1/persons/{personId}", personId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdPerson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickName").value("Nol"))
                .andExpect(jsonPath("$.job").value("REALISATOR_ACTOR"));

        // DELETE
        mockMvc.perform(delete("/v1/persons/{personId}", personId))
                .andExpect(status().isNoContent());

        // VERIFY DELETED
        mockMvc.perform(get("/v1/persons/{personId}", personId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreatePerson_usingNickNameOnly() throws Exception {
        final var personToCreate = PersonDTO.builder()
                .nickName("Kubrick")
                .job(JobEnum.REALISATOR)
                .build();

        mockMvc.perform(post("/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personToCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.nickName").value("Kubrick"))
                .andExpect(jsonPath("$.job").value("REALISATOR"));
    }

    @Test
    @DisplayName("POST /v1/persons should return 400 when name is missing (no nickname and no first+last)")
    void shouldReturnBadRequestWhenMissingNames() throws Exception {
        final var invalid = PersonDTO.builder()
                .job(JobEnum.ACTOR)
                .build();

        mockMvc.perform(post("/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /v1/persons/{id} should return 404 when unknown")
    void shouldReturnNotFoundWhenGettingUnknownPerson() throws Exception {
        mockMvc.perform(get("/v1/persons/{personId}", 999_999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /v1/persons should return all records in DB")
    void shouldReturnAllPersons() throws Exception {
        final var p1 = PersonEntity.builder().firstName("Clint").lastName("Eastwood").job(JobEnum.REALISATOR_ACTOR).build();
        final var p2 = PersonEntity.builder().nickName("Neo").job(JobEnum.ACTOR).build();
        personRepository.saveAll(List.of(p1, p2));

        mockMvc.perform(get("/v1/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("PUT /v1/persons/{id} should return 404 when unknown")
    void shouldReturnNotFoundWhenUpdatingUnknownPerson() throws Exception {
        final var update = PersonDTO.builder()
                .firstName("Someone")
                .lastName("Unknown")
                .job(JobEnum.ACTOR)
                .build();

        mockMvc.perform(put("/v1/persons/{personId}", 999_999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /v1/persons/{id} should return 400 when names are missing")
    void shouldReturnBadRequestWhenUpdatingWithMissingNames() throws Exception {
        final var person = personRepository.save(PersonEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .job(JobEnum.ACTOR)
                .build());

        final var invalidUpdate = PersonDTO.builder()
                .job(JobEnum.ACTOR)
                .build();

        mockMvc.perform(put("/v1/persons/{personId}", person.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdate)))
                .andExpect(status().isBadRequest());
    }
}

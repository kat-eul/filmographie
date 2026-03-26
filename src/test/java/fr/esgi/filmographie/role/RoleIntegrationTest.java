package fr.esgi.filmographie.role;

import fr.esgi.filmographie.role.dto.RoleDTO;
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
class RoleIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        roleRepository.deleteAll();
    }

    @Test
    void shouldCreateFindUpdateAndDeleteRole() throws Exception {
        final var roleToCreate = RoleDTO.builder()
                .name("Director")
                .build();

        // CREATE
        final var createResponse = mockMvc.perform(post("/v1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleToCreate)))
                .andExpect(status().isCreated())
                .andReturn();

        final var createdRole = objectMapper.readValue(
                createResponse.getResponse().getContentAsString(),
                RoleDTO.class
        );

        final var roleId = createdRole.getId();
        assertThat(roleId).isNotNull();

        // GET BY ID
        mockMvc.perform(get("/v1/roles/{roleId}", roleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Director"));

        // UPDATE
        createdRole.setName("Director (updated)");
        mockMvc.perform(put("/v1/roles/{roleId}", roleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdRole)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Director (updated)"));

        // DELETE
        mockMvc.perform(delete("/v1/roles/{roleId}", roleId))
                .andExpect(status().isNoContent());

        // VERIFY DELETED
        mockMvc.perform(get("/v1/roles/{roleId}", roleId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /v1/roles should return all records in DB")
    void shouldReturnAllRoles() throws Exception {
        final var r1 = RoleEntity.builder().name("Actor").build();
        final var r2 = RoleEntity.builder().name("Writer").build();
        roleRepository.saveAll(List.of(r1, r2));

        mockMvc.perform(get("/v1/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("PUT /v1/roles/{id} should return 404 when unknown")
    void shouldReturnNotFoundWhenUpdatingUnknownRole() throws Exception {
        final var update = RoleDTO.builder()
                .name("Unknown")
                .build();

        mockMvc.perform(put("/v1/roles/{roleId}", 999_999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /v1/roles/{id} should return 404 when unknown")
    void shouldReturnNotFoundWhenDeletingUnknownRole() throws Exception {
        mockMvc.perform(delete("/v1/roles/{roleId}", 999_999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /v1/roles should return 400 when name is blank")
    void shouldReturnBadRequestWhenCreatingInvalidRole() throws Exception {
        final var invalid = RoleDTO.builder()
                .name(" ")
                .build();

        mockMvc.perform(post("/v1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }
}


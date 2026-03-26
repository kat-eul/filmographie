package fr.esgi.filmographie.role.mapper;

import fr.esgi.filmographie.role.RoleEntity;
import fr.esgi.filmographie.role.dto.RoleDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class RoleMapperTest {

    private final RoleMapper mapper = Mappers.getMapper(RoleMapper.class);

    @Test
    void shouldMapEntityToDto() {
        final var entity = RoleEntity.builder()
                .id(4L)
                .name("Director")
                .build();

        final var dto = mapper.entityToDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(4L);
        assertThat(dto.getName()).isEqualTo("Director");
    }

    @Test
    void shouldMapDtoToEntity() {
        final var dto = RoleDTO.builder()
                .id(8L)
                .name("Actor")
                .build();

        final var entity = mapper.dtoToEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(8L);
        assertThat(entity.getName()).isEqualTo("Actor");
    }

    @Test
    void shouldUpdateEntityWithoutChangingId() {
        final var entity = RoleEntity.builder()
                .id(1L)
                .name("Old")
                .build();

        final var update = RoleDTO.builder()
                .id(22L)
                .name("New")
                .build();

        mapper.updateEntity(entity, update);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getName()).isEqualTo("New");
    }

    @Test
    void shouldHandleNullInputs() {
        final var entity = RoleEntity.builder()
                .id(1L)
                .name("Role")
                .build();

        assertThat(mapper.entityToDto(null)).isNull();
        assertThat(mapper.dtoToEntity(null)).isNull();

        mapper.updateEntity(entity, null);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getName()).isEqualTo("Role");
    }
}

package fr.esgi.filmographie.casting.mapper;

import fr.esgi.filmographie.casting.dto.CastingDTO;
import fr.esgi.filmographie.casting.CastingEntity;
import fr.esgi.filmographie.casting.CastingId;
import fr.esgi.filmographie.movie.MovieEntity;
import fr.esgi.filmographie.person.PersonEntity;
import fr.esgi.filmographie.role.RoleEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class CastingMapperTest {

    private final CastingMapper mapper = Mappers.getMapper(CastingMapper.class);

    @Test
    void shouldMapDtoToEntity() {
        // Given
        final var dto = CastingDTO.builder()
                .movieId(1L)
                .actorId(2L)
                .roleId(3L)
                .build();

        // When
        final var entity = mapper.toEntity(dto);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getId().getMovieId()).isEqualTo(1L);
        assertThat(entity.getId().getActorId()).isEqualTo(2L);
        assertThat(entity.getId().getRoleId()).isEqualTo(3L);
    }

    @Test
    @DisplayName("should map entity to dto with nested fields")
    void shouldMapEntityToDto() {
        final var entity = CastingEntity.builder()
                .id(new CastingId(1L, 3L, 2L))
                .movie(MovieEntity.builder().id(1L).title("Inception").build())
                .role(RoleEntity.builder().id(3L).name("Dom Cobb").build())
                .actor(PersonEntity.builder().id(2L).nickName("Leo").build())
                .build();

        final var dto = mapper.toDTO(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getMovieId()).isEqualTo(1L);
        assertThat(dto.getRoleId()).isEqualTo(3L);
        assertThat(dto.getActorId()).isEqualTo(2L);
        assertThat(dto.getMovieTitle()).isEqualTo("Inception");
        assertThat(dto.getRoleName()).isEqualTo("Dom Cobb");
        assertThat(dto.getActorName()).isEqualTo("Leo");
    }

    @Test
    @DisplayName("should return null when dto is null")
    void shouldReturnNullWhenDtoIsNull() {
        assertThat(mapper.toEntity(null)).isNull();
    }

    @Test
    @DisplayName("should return null when entity is null")
    void shouldReturnNullWhenEntityIsNull() {
        assertThat(mapper.toDTO(null)).isNull();
    }
}

package fr.esgi.filmographie.casting.mapper;

import fr.esgi.filmographie.casting.dto.CastingDTO;
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
}


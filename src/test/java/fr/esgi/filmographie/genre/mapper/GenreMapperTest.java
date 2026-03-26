package fr.esgi.filmographie.genre.mapper;

import fr.esgi.filmographie.genre.GenreEntity;
import fr.esgi.filmographie.genre.dto.GenreDTO;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

class GenreMapperTest {

    private final GenreMapper mapper = Mappers.getMapper(GenreMapper.class);

    @Test
    void shouldMapDtoToEntity() {
        final var dto = GenreDTO.builder()
                .id(3L)
                .name("Science-fiction")
                .build();

        final var entity = mapper.dtoToEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(3L);
        assertThat(entity.getName()).isEqualTo("Science-fiction");
        assertThat(entity.getMovies()).isEmpty();
    }

    @Test
    void shouldMapEntityToDto() {
        final var entity = GenreEntity.builder()
                .id(7L)
                .name("Drame")
                .build();

        final var dto = mapper.entityToDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(7L);
        assertThat(dto.getName()).isEqualTo("Drame");
    }

    @Test
    void shouldUpdateEntityWithoutChangingIgnoredFields() {
        final var entity = GenreEntity.builder()
                .id(11L)
                .name("Old")
                .build();
        entity.getMovies().add(null);

        final var update = GenreDTO.builder()
                .id(99L)
                .name("New")
                .build();

        mapper.updateEntity(entity, update);

        assertThat(entity.getId()).isEqualTo(11L);
        assertThat(entity.getName()).isEqualTo("New");
        assertThat(entity.getMovies()).hasSize(1);
    }

    @Test
    void shouldHandleNullInputs() {
        final var entity = GenreEntity.builder()
                .id(1L)
                .name("Action")
                .build();

        assertThat(mapper.dtoToEntity(null)).isNull();
        assertThat(mapper.entityToDto(null)).isNull();

        mapper.updateEntity(entity, null);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getName()).isEqualTo("Action");
    }
}


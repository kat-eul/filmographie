package fr.esgi.filmographie.movie.mapper;

import fr.esgi.filmographie.genre.GenreEntity;
import fr.esgi.filmographie.movie.MovieEntity;
import fr.esgi.filmographie.movie.dto.MovieDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MovieMapperTest {

    @Autowired
    private MovieMapper mapper;

    @Test
    void shouldMapDtoToEntityWithoutGenres() {
        final var dto = new MovieDTO(
                10L,
                "Inception",
                LocalDate.of(2010,7,16),
                "Dreams inside dreams"
        );

        final var entity = mapper.dtoToEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(10L);
        assertThat(entity.getTitle()).isEqualTo("Inception");
        assertThat(entity.getReleaseDate()).isEqualTo(LocalDate.of(2010,7,16));
        assertThat(entity.getSummary()).isEqualTo("Dreams inside dreams");
    }

    @Test
    void shouldMapEntityToDto() {
        final var entity = MovieEntity.builder()
                .id(12L)
                .title("Interstellar")
                .releaseDate(LocalDate.of(2014, 11, 7))
                .summary("Space and time")
                .build();

        final var dto = mapper.entityToDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto)
                .extracting("id", "title", "releaseDate", "summary")
                .containsExactly(12L, "Interstellar", LocalDate.of(2014, 11, 7), "Space and time");
    }

    @Test
    void shouldMapEntityToWithAllInfoDtoIncludingGenres() {
        final var movie = MovieEntity.builder()
                .id(20L)
                .title("The Matrix")
                .releaseDate(LocalDate.of(1999, 3, 31))
                .summary("Neo discovers the truth")
                .build();
        movie.getGenres().add(GenreEntity.builder().id(1L).name("Action").build());
        movie.getGenres().add(GenreEntity.builder().id(2L).name("Sci-Fi").build());

        final var dto = mapper.entityToWithAllInfoDto(movie);

        assertThat(dto).isNotNull();
        assertThat(dto)
                .extracting("id", "title")
                .containsExactly(20L, "The Matrix");
        assertThat(dto.getGenres()).isNotNull().hasSize(2);
        assertThat(dto.getGenres())
                .extracting("name")
                .containsExactly("Action", "Sci-Fi");
    }

    @Test
    void shouldUpdateEntityWithoutChangingIgnoredFields() {
        final var entity = MovieEntity.builder()
                .id(30L)
                .title("Old title")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .summary("Old summary")
                .build();
        entity.getGenres().add(GenreEntity.builder().id(99L).name("Keep").build());

        final var update = new MovieDTO(
                123L,
                "New title",
                LocalDate.of(2001, 2, 2),
                "New summary"
        );

        mapper.updateEntity(entity, update);

        assertThat(entity.getId()).isEqualTo(30L);
        assertThat(entity.getTitle()).isEqualTo("New title");
        assertThat(entity.getReleaseDate()).isEqualTo(LocalDate.of(2001, 2, 2));
        assertThat(entity.getSummary()).isEqualTo("New summary");
        assertThat(entity.getGenres()).hasSize(1);
        assertThat(entity.getGenres().getFirst().getName()).isEqualTo("Keep");
    }

    @Test
    void shouldHandleNullInputs() {
        final var entity = MovieEntity.builder()
                .id(1L)
                .title("Title")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .summary("Summary")
                .build();

        assertThat(mapper.dtoToEntity(null)).isNull();
        assertThat(mapper.entityToDto(null)).isNull();
        assertThat(mapper.entityToWithAllInfoDto(null)).isNull();

        mapper.updateEntity(entity, null);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getTitle()).isEqualTo("Title");
    }
}

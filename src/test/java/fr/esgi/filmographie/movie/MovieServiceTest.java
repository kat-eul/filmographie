package fr.esgi.filmographie.movie;

import fr.esgi.filmographie.exception.NotFoundException;
import fr.esgi.filmographie.movie.dto.MovieDTO;
import fr.esgi.filmographie.movie.exception.MovieNotFoundException;
import fr.esgi.filmographie.movie.mapper.MovieMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieMapper movieMapper;

    @Nested
    @DisplayName("getAll tests")
    class GetAllTests {
        @Test
        void shouldReturnAllMoviesAsDTOs() {
            final var entity1 = MovieEntity.builder().id(1L).title("Inception").build();
            final var entity2 = MovieEntity.builder().id(2L).title("Interstellar").build();
            final var dto1 = MovieDTO.builder().id(1L).title("Inception").build();
            final var dto2 = MovieDTO.builder().id(2L).title("Interstellar").build();

            doReturn(List.of(entity1, entity2)).when(movieRepository).findAll();
            doReturn(dto1).when(movieMapper).entityToDto(entity1);
            doReturn(dto2).when(movieMapper).entityToDto(entity2);

            final var result = movieService.getAll();

            assertThat(result).hasSize(2).containsExactly(dto1, dto2);
            verify(movieRepository).findAll();
        }
    }

    @Nested
    @DisplayName("getById tests")
    class GetByIdTests {
        @Test
        void shouldReturnMovieDTOWhenFound() throws NotFoundException {
            final var id = 1L;
            final var entity = MovieEntity.builder().id(id).build();
            final var dto = MovieDTO.builder().id(id).build();

            doReturn(Optional.of(entity)).when(movieRepository).findById(id);
            doReturn(dto).when(movieMapper).entityToDto(entity);

            final var result = movieService.getById(id);

            assertThat(result).isEqualTo(dto);
        }

        @Test
        void shouldThrowExceptionWhenNotFound() {
            final var id = 99L;
            doReturn(Optional.empty()).when(movieRepository).findById(id);

            assertThatThrownBy(() -> movieService.getById(id))
                    .isInstanceOf(MovieNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("create tests")
    class CreateTests {
        @Test
        void shouldSaveAndReturnDTO() {
            final var inputDto = MovieDTO.builder().title("Tenet").build();
            final var entity = MovieEntity.builder().title("Tenet").build();
            final var savedEntity = MovieEntity.builder().id(1L).title("Tenet").build();
            final var outputDto = MovieDTO.builder().id(1L).title("Tenet").build();

            doReturn(entity).when(movieMapper).dtoToEntity(inputDto);
            doReturn(savedEntity).when(movieRepository).save(entity);
            doReturn(outputDto).when(movieMapper).entityToDto(savedEntity);

            final var result = movieService.create(inputDto);

            assertThat(result).isEqualTo(outputDto);
            verify(movieRepository).save(entity);
        }
    }

    @Nested
    @DisplayName("update tests")
    class UpdateTests {
        @Test
        void shouldUpdateExistingMovie() throws NotFoundException {
            final var id = 1L;
            final var updateInfo = MovieDTO.builder().title("New Title").build();
            final var existingMovie = MovieEntity.builder().id(id).title("Old Title").build();
            final var updatedDto = MovieDTO.builder().id(id).title("New Title").build();

            doReturn(Optional.of(existingMovie)).when(movieRepository).findById(id);
            doReturn(existingMovie).when(movieRepository).save(existingMovie);
            doReturn(updatedDto).when(movieMapper).entityToDto(existingMovie);

            final var result = movieService.update(id, updateInfo);

            assertThat(result.getTitle()).isEqualTo("New Title");
            verify(movieRepository).save(existingMovie);
        }
    }

    @Nested
    @DisplayName("delete tests")
    class DeleteTests {
        @Test
        void shouldDeleteWhenExists() throws NotFoundException {
            final var id = 1L;
            doReturn(true).when(movieRepository).existsById(id);

            movieService.delete(id);

            verify(movieRepository).deleteById(id);
        }

        @Test
        void shouldThrowExceptionWhenDeletingNonExistent() {
            final var id = 1L;
            doReturn(false).when(movieRepository).existsById(id);

            assertThatThrownBy(() -> movieService.delete(id))
                    .isInstanceOf(MovieNotFoundException.class);

            verify(movieRepository, never()).deleteById(anyLong());
        }
    }
}
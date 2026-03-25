package fr.esgi.filmographie.genre;

import fr.esgi.filmographie.exception.NotFoundException;
import fr.esgi.filmographie.genre.dto.GenreDTO;
import fr.esgi.filmographie.genre.exception.GenreNotFoundException;
import fr.esgi.filmographie.genre.mapper.GenreMapper;
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
class GenreServiceTest {

    @InjectMocks
    private GenreService genreService;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private GenreMapper genreMapper;

    @Nested
    @DisplayName("getAll tests")
    class GetAllTests {
        @Test
        void shouldReturnAllMoviesAsDTOs() {
            final var entity1 = GenreEntity.builder().id(1L).name("Action").build();
            final var entity2 = GenreEntity.builder().id(2L).name("Comedy").build();
            final var dto1 = GenreDTO.builder().id(1L).name("Action").build();
            final var dto2 = GenreDTO.builder().id(2L).name("Comedy").build();

            doReturn(List.of(entity1, entity2)).when(genreRepository).findAll();
            doReturn(dto1).when(genreMapper).entityToDto(entity1);
            doReturn(dto2).when(genreMapper).entityToDto(entity2);

            final var result = genreService.getAll();

            assertThat(result).hasSize(2).containsExactly(dto1, dto2);
            verify(genreRepository).findAll();
        }
    }

    @Nested
    @DisplayName("getById tests")
    class GetByIdTests {
        @Test
        void shouldReturnGenreDTOWhenFound() throws NotFoundException {
            final var id = 1L;
            final var entity = GenreEntity.builder().id(id).build();
            final var dto = GenreDTO.builder().id(id).build();

            doReturn(Optional.of(entity)).when(genreRepository).findById(id);
            doReturn(dto).when(genreMapper).entityToDto(entity);

            final var result = genreService.getById(id);

            assertThat(result).isEqualTo(dto);
        }

        @Test
        void shouldThrowExceptionWhenNotFound() {
            final var id = 99L;
            doReturn(Optional.empty()).when(genreRepository).findById(id);

            assertThatThrownBy(() -> genreService.getById(id))
                    .isInstanceOf(GenreNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("create tests")
    class CreateTests {
        @Test
        void shouldSaveAndReturnDTO() {
            final var inputDto = GenreDTO.builder().name("Action").build();
            final var entity = GenreEntity.builder().name("Action").build();
            final var savedEntity = GenreEntity.builder().id(1L).name("Action").build();
            final var outputDto = GenreDTO.builder().id(1L).name("Action").build();

            doReturn(entity).when(genreMapper).dtoToEntity(inputDto);
            doReturn(savedEntity).when(genreRepository).save(entity);
            doReturn(outputDto).when(genreMapper).entityToDto(savedEntity);

            final var result = genreService.create(inputDto);

            assertThat(result).isEqualTo(outputDto);
            verify(genreRepository).save(entity);
        }
    }

    @Nested
    @DisplayName("update tests")
    class UpdateTests {
        @Test
        void shouldUpdateExistingMovie() throws NotFoundException {
            final var id = 1L;
            final var updateInfo = GenreDTO.builder().name("Superhero").build();
            final var existingMovie = GenreEntity.builder().id(id).name("Action").build();
            final var updatedDto = GenreDTO.builder().id(id).name("Superhero").build();

            doReturn(Optional.of(existingMovie)).when(genreRepository).findById(id);
            doReturn(existingMovie).when(genreRepository).save(existingMovie);
            doReturn(updatedDto).when(genreMapper).entityToDto(existingMovie);

            final var result = genreService.update(id, updateInfo);

            assertThat(result.getName()).isEqualTo("Superhero");
            verify(genreRepository).save(existingMovie);
        }
    }

    @Nested
    @DisplayName("delete tests")
    class DeleteTests {
        @Test
        void shouldDeleteWhenExists() throws NotFoundException {
            final var id = 1L;
            doReturn(true).when(genreRepository).existsById(id);

            genreService.delete(id);

            verify(genreRepository).deleteById(id);
        }

        @Test
        void shouldThrowExceptionWhenDeletingNonExistent() {
            final var id = 1L;
            doReturn(false).when(genreRepository).existsById(id);

            assertThatThrownBy(() -> genreService.delete(id))
                    .isInstanceOf(GenreNotFoundException.class);

            verify(genreRepository, never()).deleteById(anyLong());
        }
    }
}
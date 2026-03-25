package fr.esgi.filmographie.role;

import fr.esgi.filmographie.exception.NotFoundException;
import fr.esgi.filmographie.role.dto.RoleDTO;
import fr.esgi.filmographie.role.exception.RoleNotFoundException;
import fr.esgi.filmographie.role.mapper.RoleMapper;
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
class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @Nested
    @DisplayName("getAll tests")
    class GetAllTests {
        @Test
        void shouldReturnAllMoviesAsDTOs() {
            final var id1 = 1L;
            final var id2 = 2L;
            final var name1 = "Le Joker";
            final var name2 = "Batman";
            final var entity1 = RoleEntity.builder().id(id1).name(name1).build();
            final var entity2 = RoleEntity.builder().id(id2).name(name2).build();
            final var dto1 = RoleDTO.builder().id(id1).name(name1).build();
            final var dto2 = RoleDTO.builder().id(id2).name(name2).build();

            doReturn(List.of(entity1, entity2)).when(roleRepository).findAll();
            doReturn(dto1).when(roleMapper).entityToDto(entity1);
            doReturn(dto2).when(roleMapper).entityToDto(entity2);

            final var result = roleService.getAll();

            assertThat(result).hasSize(2).containsExactly(dto1, dto2);
            verify(roleRepository).findAll();
        }
    }

    @Nested
    @DisplayName("getById tests")
    class GetByIdTests {
        @Test
        void shouldReturnMovieDTOWhenFound() throws NotFoundException {
            final var id = 1L;
            final var entity = RoleEntity.builder().id(id).build();
            final var dto = RoleDTO.builder().id(id).build();

            doReturn(Optional.of(entity)).when(roleRepository).findById(id);
            doReturn(dto).when(roleMapper).entityToDto(entity);

            final var result = roleService.getById(id);

            assertThat(result).isEqualTo(dto);
        }

        @Test
        void shouldThrowExceptionWhenNotFound() {
            final var id = 99L;
            doReturn(Optional.empty()).when(roleRepository).findById(id);

            assertThatThrownBy(() -> roleService.getById(id))
                    .isInstanceOf(RoleNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("create tests")
    class CreateTests {
        @Test
        void shouldSaveAndReturnDTO() {
            final var id = 1L;
            final var name = "Batman";
            final var inputDto = RoleDTO.builder().name(name).build();
            final var entity = RoleEntity.builder().name(name).build();
            final var savedEntity = RoleEntity.builder().id(id).name(name).build();
            final var outputDto = RoleDTO.builder().id(id).name(name).build();

            doReturn(entity).when(roleMapper).dtoToEntity(inputDto);
            doReturn(savedEntity).when(roleRepository).save(entity);
            doReturn(outputDto).when(roleMapper).entityToDto(savedEntity);

            final var result = roleService.create(inputDto);

            assertThat(result).isEqualTo(outputDto);
            verify(roleRepository).save(entity);
        }
    }

    @Nested
    @DisplayName("update tests")
    class UpdateTests {
        @Test
        void shouldUpdateExistingMovie() throws NotFoundException {
            final var id = 1L;
            final var name = "Batman";
            final var newName = "Le Joker";
            final var updateInfo = RoleDTO.builder().name(newName).build();
            final var existingMovie = RoleEntity.builder().id(id).name(name).build();
            final var updatedDto = RoleDTO.builder().id(id).name(newName).build();

            doReturn(Optional.of(existingMovie)).when(roleRepository).findById(id);
            doReturn(existingMovie).when(roleRepository).save(existingMovie);
            doReturn(updatedDto).when(roleMapper).entityToDto(existingMovie);

            final var result = roleService.update(id, updateInfo);

            assertThat(result.getName()).isEqualTo(newName);
            verify(roleRepository).save(existingMovie);
        }
    }

    @Nested
    @DisplayName("delete tests")
    class DeleteTests {
        @Test
        void shouldDeleteWhenExists() throws NotFoundException {
            final var id = 1L;
            doReturn(true).when(roleRepository).existsById(id);

            roleService.delete(id);

            verify(roleRepository).deleteById(id);
        }

        @Test
        void shouldThrowExceptionWhenDeletingNonExistent() {
            final var id = 1L;
            doReturn(false).when(roleRepository).existsById(id);

            assertThatThrownBy(() -> roleService.delete(id))
                    .isInstanceOf(RoleNotFoundException.class);

            verify(roleRepository, never()).deleteById(anyLong());
        }
    }
}
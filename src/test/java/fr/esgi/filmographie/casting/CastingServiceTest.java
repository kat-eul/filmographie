package fr.esgi.filmographie.casting;

import fr.esgi.filmographie.casting.dto.CastingDTO;
import fr.esgi.filmographie.casting.mapper.CastingMapper;
import fr.esgi.filmographie.enums.JobEnum;
import fr.esgi.filmographie.genre.GenreEntity;
import fr.esgi.filmographie.movie.MovieEntity;
import fr.esgi.filmographie.person.PersonEntity;
import fr.esgi.filmographie.role.RoleEntity;
import fr.esgi.filmographie.exception.NotFoundException;
import fr.esgi.filmographie.casting.exception.CastingNotFoundException;
import fr.esgi.filmographie.casting.exception.NotAnActorException;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CastingServiceTest {

    @InjectMocks
    private CastingService castingService;

    @Mock
    private CastingRepository castingRepository;

    @Mock
    private CastingMapper castingMapper;

    private CastingId castingId1;
    private CastingId castingId2;
    private CastingEntity entity1;
    private CastingEntity entity2;
    private CastingDTO dto1;
    private CastingDTO dto2;

    @BeforeEach
    void setUp() {
        final var movieEntity = MovieEntity.builder().id(1L).title("Inception").build();
        final var roleEntity1 = RoleEntity.builder().id(2L).name("Dom Cobb").build();
        final var roleEntity2 = RoleEntity.builder().id(3L).name("Un autre role").build();
        final var actorEntity = PersonEntity.builder().id(4L).job(JobEnum.REALISATOR).firstName("Leonardo").lastName("DiCaprio").nickName("Leo").build();
        final var secondActorEntity = PersonEntity.builder().id(5L).job(JobEnum.REALISATOR).firstName("Joseph").lastName("Gordon-Levitt").nickName("JGL").build();

        castingId1 = new CastingId(movieEntity.getId(), roleEntity1.getId(), actorEntity.getId());
        castingId2 = new CastingId(movieEntity.getId(), roleEntity2.getId(), secondActorEntity.getId());
        entity1 = CastingEntity.builder().id(castingId1).movie(movieEntity).role(roleEntity1).actor(actorEntity).build();
        entity2 = CastingEntity.builder().id(castingId2).movie(movieEntity).role(roleEntity2).actor(secondActorEntity).build();

        dto1 = CastingDTO.builder()
                .movieId(movieEntity.getId()).movieTitle(movieEntity.getTitle())
                .roleId(roleEntity1.getId()).roleName(roleEntity1.getName())
                .actorId(actorEntity.getId()).actorName(actorEntity.getNickName()).build();
        dto2 = CastingDTO.builder()
                .movieId(movieEntity.getId()).movieTitle(movieEntity.getTitle())
                .roleId(roleEntity2.getId()).roleName(roleEntity2.getName())
                .actorId(secondActorEntity.getId()).actorName(secondActorEntity.getNickName()).build();

    }

    @Nested
    @DisplayName("getAll tests")
    class GetAllTests {
        @Test
        void shouldReturnAllCastingAsDTOs() {
            doReturn(List.of(entity1, entity2)).when(castingRepository).findAll();
            doReturn(dto1).when(castingMapper).toDTO(entity1);
            doReturn(dto2).when(castingMapper).toDTO(entity2);

            final var result = castingService.getAll();

            assertThat(result).hasSize(2).containsExactly(dto1, dto2);
            verify(castingRepository).findAll();
        }
    }

    @Nested
    @DisplayName("getById tests")
    class GetByIdTests {
        @Test
        void shouldReturnGenreDTOWhenFound() throws NotFoundException {
            doReturn(Optional.of(entity1)).when(castingRepository).findById(castingId1);
            doReturn(dto1).when(castingMapper).toDTO(entity1);

            final var result = castingService.getById(
                    entity1.getId().getMovieId(),
                    entity1.getId().getRoleId(),
                    entity1.getId().getActorId()
            );

            assertThat(result).isEqualTo(dto1);
        }
        void shouldThrowExceptionWhenNotFound() {
            doReturn(Optional.empty()).when(castingRepository).findById(castingId1);

            assertThatThrownBy(() -> castingService.getById(
                    castingId1.getMovieId(),
                    castingId1.getRoleId(),
                    castingId1.getActorId()
            )).isInstanceOf(CastingNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("create tests")
    class CreateTests {
        @Test
        void shouldCreateCastingWhenJobIsNotAllowed() {
            doReturn(entity1).when(castingMapper).toEntity(dto1);
            doReturn(entity1).when(castingRepository).save(entity1);
            doReturn(dto1).when(castingMapper).toDTO(entity1);

            final var result = castingService.create(dto1);

            assertThat(result).isEqualTo(dto1);
            verify(castingRepository).save(entity1);
        }

        @Test
        void shouldThrowWhenActorJobIsAllowedActor() {
            final var actor = PersonEntity.builder().id(99L).firstName("Tom").lastName("Hardy").job(JobEnum.ACTOR).build();
            final var invalidEntity = CastingEntity.builder().id(castingId1).movie(entity1.getMovie()).role(entity1.getRole()).actor(actor).build();
            doReturn(invalidEntity).when(castingMapper).toEntity(dto1);

            assertThatThrownBy(() -> castingService.create(dto1)).isInstanceOf(NotAnActorException.class);
            verify(castingRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("getBy filters tests")
    class GetByFiltersTests {
        @Test
        void shouldGetByMovieId() {
            doReturn(List.of(entity1)).when(castingRepository).findByMovieId(1L);
            doReturn(dto1).when(castingMapper).toDTO(entity1);

            final var result = castingService.getByMovieId(1L);

            assertThat(result).containsExactly(dto1);
            verify(castingRepository).findByMovieId(1L);
        }

        @Test
        void shouldGetByRoleId() {
            doReturn(List.of(entity2)).when(castingRepository).findByRoleId(3L);
            doReturn(dto2).when(castingMapper).toDTO(entity2);

            final var result = castingService.getByRoleId(3L);

            assertThat(result).containsExactly(dto2);
            verify(castingRepository).findByRoleId(3L);
        }
    }

    @Nested
    @DisplayName("update tests")
    class UpdateTests {
        @Test
        void shouldUpdateAllFields() {
            final var updatedMovie = MovieEntity.builder().id(10L).title("Interstellar").build();
            final var updatedRole = RoleEntity.builder().id(20L).name("Cooper").build();
            final var updatedActor = PersonEntity.builder().id(30L).firstName("Matthew").lastName("McConaughey").job(JobEnum.REALISATOR).build();
            final var updateEntity = CastingEntity.builder().id(castingId1).movie(updatedMovie).role(updatedRole).actor(updatedActor).build();
            final var updatedResult = CastingEntity.builder().id(castingId1).movie(updatedMovie).role(updatedRole).actor(updatedActor).build();

            doReturn(Optional.of(entity1)).when(castingRepository).findById(castingId1);
            doReturn(updateEntity).when(castingMapper).toEntity(dto1);
            doReturn(updatedResult).when(castingRepository).save(entity1);
            doReturn(dto1).when(castingMapper).toDTO(updatedResult);

            final var result = castingService.update(castingId1.getMovieId(), castingId1.getRoleId(), castingId1.getActorId(), dto1);

            assertThat(result).isEqualTo(dto1);
            assertThat(entity1.getMovie()).isEqualTo(updatedMovie);
            assertThat(entity1.getRole()).isEqualTo(updatedRole);
            assertThat(entity1.getActor()).isEqualTo(updatedActor);
        }

        @Test
        void shouldThrowWhenUpdateTargetNotFound() {
            doReturn(Optional.empty()).when(castingRepository).findById(castingId1);
            doReturn(entity1).when(castingMapper).toEntity(dto1);

            assertThatThrownBy(() -> castingService.update(castingId1.getMovieId(), castingId1.getRoleId(), castingId1.getActorId(), dto1))
                    .isInstanceOf(CastingNotFoundException.class);
            verify(castingRepository, never()).save(any());
        }

        @Test
        void shouldThrowWhenUpdatedActorJobIsAllowedActor() {
            final var invalidActor = PersonEntity.builder().id(88L).firstName("Cillian").lastName("Murphy").job(JobEnum.ACTOR).build();
            final var updateEntity = CastingEntity.builder().id(castingId1).actor(invalidActor).build();

            doReturn(Optional.of(entity1)).when(castingRepository).findById(castingId1);
            doReturn(updateEntity).when(castingMapper).toEntity(dto1);

            assertThatThrownBy(() -> castingService.update(castingId1.getMovieId(), castingId1.getRoleId(), castingId1.getActorId(), dto1))
                    .isInstanceOf(NotAnActorException.class);
            verify(castingRepository, never()).save(any());
        }

        @Test
        void shouldKeepFieldsWhenIncomingEntityHasNulls() {
            final var updateEntity = CastingEntity.builder().id(castingId1).movie(null).role(null).actor(null).build();
            doReturn(Optional.of(entity1)).when(castingRepository).findById(castingId1);
            doReturn(updateEntity).when(castingMapper).toEntity(dto1);
            doReturn(entity1).when(castingRepository).save(entity1);
            doReturn(dto1).when(castingMapper).toDTO(entity1);

            final var result = castingService.update(castingId1.getMovieId(), castingId1.getRoleId(), castingId1.getActorId(), dto1);

            assertThat(result).isEqualTo(dto1);
            verify(castingRepository).save(entity1);
        }
    }

    @Nested
    @DisplayName("delete tests")
    class DeleteTests {
        @Test
        void shouldDeleteWhenFound() {
            doReturn(Optional.of(entity1)).when(castingRepository).findById(castingId1);

            castingService.delete(castingId1.getMovieId(), castingId1.getRoleId(), castingId1.getActorId());

            verify(castingRepository).delete(entity1);
        }

        @Test
        void shouldThrowWhenDeleteTargetNotFound() {
            doReturn(Optional.empty()).when(castingRepository).findById(castingId1);

            assertThatThrownBy(() -> castingService.delete(castingId1.getMovieId(), castingId1.getRoleId(), castingId1.getActorId()))
                    .isInstanceOf(CastingNotFoundException.class);
            verify(castingRepository, never()).delete(any());
        }
    }
}

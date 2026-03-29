package fr.esgi.filmographie.casting;

import fr.esgi.filmographie.casting.dto.CastingDTO;
import fr.esgi.filmographie.casting.mapper.CastingMapper;
import fr.esgi.filmographie.enums.JobEnum;
import fr.esgi.filmographie.movie.MovieEntity;
import fr.esgi.filmographie.movie.MovieRepository;
import fr.esgi.filmographie.person.PersonEntity;
import fr.esgi.filmographie.person.PersonRepository;
import fr.esgi.filmographie.role.RoleEntity;
import fr.esgi.filmographie.role.RoleRepository;
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
    private MovieRepository movieRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private CastingMapper castingMapper;

    private CastingId castingId1;
    private CastingEntity entity1;
    private CastingEntity entity2;
    private CastingDTO dto1;
    private CastingDTO dto2;
    private MovieEntity movieEntity;
    private RoleEntity roleEntity1;
    private RoleEntity roleEntity2;
    private PersonEntity actorEntity;
    private PersonEntity secondActorEntity;

    @BeforeEach
    void setUp() {
        movieEntity = MovieEntity.builder().id(1L).title("Inception").build();
        roleEntity1 = RoleEntity.builder().id(2L).name("Dom Cobb").build();
        roleEntity2 = RoleEntity.builder().id(3L).name("Un autre role").build();
        actorEntity = PersonEntity.builder().id(4L).job(JobEnum.ACTOR).firstName("Leonardo").lastName("DiCaprio").nickName("Leo").build();
        secondActorEntity = PersonEntity.builder().id(5L).job(JobEnum.ACTOR).firstName("Joseph").lastName("Gordon-Levitt").nickName("JGL").build();

        castingId1 = new CastingId(movieEntity.getId(), roleEntity1.getId(), actorEntity.getId());
        CastingId castingId2 = new CastingId(movieEntity.getId(), roleEntity2.getId(), secondActorEntity.getId());
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

        @Test
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

    @Nested
    @DisplayName("create tests")
    class CreateTests {
        @Test
        void shouldCreateCastingWhenActorJobIsAllowedActor() {
            doReturn(Optional.of(movieEntity)).when(movieRepository).findById(dto1.getMovieId());
            doReturn(Optional.of(roleEntity1)).when(roleRepository).findById(dto1.getRoleId());
            doReturn(Optional.of(actorEntity)).when(personRepository).findById(dto1.getActorId());
            doReturn(entity1).when(castingRepository).save(any(CastingEntity.class));
            doReturn(dto1).when(castingMapper).toDTO(entity1);

            final var result = castingService.create(dto1);

            assertThat(result).isEqualTo(dto1);
            verify(movieRepository).findById(dto1.getMovieId());
            verify(roleRepository).findById(dto1.getRoleId());
            verify(personRepository).findById(dto1.getActorId());
            verify(castingRepository).save(any(CastingEntity.class));
        }

        @Test
        void shouldThrowWhenActorJobIsNotAllowedActor() {
            final var director = PersonEntity.builder().id(99L).job(JobEnum.REALISATOR).firstName("Chris").lastName("Nolan").build();
            final var invalidDto = CastingDTO.builder()
                    .movieId(movieEntity.getId())
                    .roleId(roleEntity1.getId())
                    .actorId(director.getId())
                    .build();

            doReturn(Optional.of(movieEntity)).when(movieRepository).findById(invalidDto.getMovieId());
            doReturn(Optional.of(roleEntity1)).when(roleRepository).findById(invalidDto.getRoleId());
            doReturn(Optional.of(director)).when(personRepository).findById(invalidDto.getActorId());

            assertThatThrownBy(() -> castingService.create(invalidDto))
                    .isInstanceOf(NotAnActorException.class);
            verify(castingRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("update tests")
    class UpdateTests {
        @Test
        void shouldUpdateCastingWhenFound() {
            doReturn(Optional.of(entity1)).when(castingRepository).findById(castingId1);
            doReturn(Optional.of(movieEntity)).when(movieRepository).findById(dto2.getMovieId());
            doReturn(Optional.of(roleEntity2)).when(roleRepository).findById(dto2.getRoleId());
            doReturn(Optional.of(secondActorEntity)).when(personRepository).findById(dto2.getActorId());
            doReturn(entity2).when(castingRepository).save(any(CastingEntity.class));
            doReturn(dto2).when(castingMapper).toDTO(entity2);

            final var result = castingService.update(castingId1.getMovieId(), castingId1.getRoleId(), castingId1.getActorId(), dto2);

            assertThat(result).isEqualTo(dto2);
            verify(castingRepository).findById(castingId1);
            verify(movieRepository).findById(dto2.getMovieId());
            verify(roleRepository).findById(dto2.getRoleId());
            verify(personRepository).findById(dto2.getActorId());
            verify(castingRepository).save(any(CastingEntity.class));
        }

        @Test
        void shouldThrowWhenUpdateTargetNotFound() {
            doReturn(Optional.empty()).when(castingRepository).findById(castingId1);

            assertThatThrownBy(() -> castingService.update(castingId1.getMovieId(), castingId1.getRoleId(), castingId1.getActorId(), dto1))
                    .isInstanceOf(CastingNotFoundException.class);
            verify(castingRepository, never()).save(any());
        }
    }
}

package fr.esgi.filmographie.casting;

import fr.esgi.filmographie.casting.dto.CastingDTO;
import fr.esgi.filmographie.casting.mapper.CastingMapper;
import fr.esgi.filmographie.enums.JobEnum;
import fr.esgi.filmographie.genre.GenreEntity;
import fr.esgi.filmographie.movie.MovieEntity;
import fr.esgi.filmographie.person.PersonEntity;
import fr.esgi.filmographie.role.RoleEntity;
import fr.esgi.filmographie.exception.NotFoundException;
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
import static org.mockito.Mockito.anyLong;
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
        final var personEntity = PersonEntity.builder().id(4L).job(JobEnum.REALISATOR_ACTOR).firstName("Leonardo").lastName("DiCaprio").build();
        final var personNotActorEntity = PersonEntity.builder().id(5L).job(JobEnum.REALISATOR).firstName("Christopher").lastName("Nolan").build();

        castingId1 = new CastingId(movieEntity.getId(), roleEntity1.getId(), personEntity.getId());
        castingId2 = new CastingId(movieEntity.getId(), roleEntity2.getId(), personEntity.getId());
        entity1 = CastingEntity.builder().id(castingId1).movie(movieEntity).role(roleEntity1).actor(personEntity).build();
        entity2 = CastingEntity.builder().id(castingId2).movie(movieEntity).role(roleEntity2).actor(personEntity).build();

        dto1 = CastingDTO.builder()
                .movieId(movieEntity.getId()).movieTitle(movieEntity.getTitle())
                .roleId(roleEntity1.getId()).roleName(roleEntity1.getName())
                .actorId(personEntity.getId()).actorName(personEntity.getFirstName()+" "+personEntity.getLastName()).build();
        dto2 = CastingDTO.builder()
                .movieId(movieEntity.getId()).movieTitle(movieEntity.getTitle())
                .roleId(roleEntity2.getId()).roleName(roleEntity2.getName())
                .actorId(personEntity.getId()).actorName(personEntity.getFirstName()+" "+personEntity.getLastName()).build();

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
            )).isInstanceOf(fr.esgi.filmographie.casting.exception.CastingNotFoundException.class);
        }
    }
}

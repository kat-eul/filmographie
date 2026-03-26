package fr.esgi.filmographie.person.mapper;

import fr.esgi.filmographie.enums.JobEnum;
import fr.esgi.filmographie.person.PersonEntity;
import fr.esgi.filmographie.person.dto.PersonDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PersonMapperTest {

    private final PersonMapper mapper = Mappers.getMapper(PersonMapper.class);

    @Test
    void shouldMapDtoToEntity() {
        final var dto = PersonDTO.builder()
                .id(2L)
                .firstName("Keanu")
                .lastName("Reeves")
                .nickName("Neo")
                .job(JobEnum.ACTOR)
                .build();

        final var entity = mapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(2L);
        assertThat(entity.getFirstName()).isEqualTo("Keanu");
        assertThat(entity.getLastName()).isEqualTo("Reeves");
        assertThat(entity.getNickName()).isEqualTo("Neo");
        assertThat(entity.getJob()).isEqualTo(JobEnum.ACTOR);
    }

    @Test
    void shouldMapEntityAndListToDto() {
        final var first = PersonEntity.builder()
                .id(1L)
                .firstName("Christopher")
                .lastName("Nolan")
                .job(JobEnum.REALISATOR)
                .build();
        final var second = PersonEntity.builder()
                .id(2L)
                .firstName("Clint")
                .lastName("Eastwood")
                .job(JobEnum.REALISATOR_ACTOR)
                .build();

        final var dto = mapper.toDto(first);
        final var dtos = mapper.toDto(List.of(first, second));

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getLastName()).isEqualTo("Nolan");

        assertThat(dtos).isNotNull();
        assertThat(dtos).hasSize(2);
        assertThat(dtos).extracting(PersonDTO::getLastName).containsExactly("Nolan", "Eastwood");
        assertThat(dtos).extracting(PersonDTO::getJob)
                .containsExactly(JobEnum.REALISATOR, JobEnum.REALISATOR_ACTOR);
    }

    @Test
    void shouldUpdateEntityWithoutChangingId() {
        final var entity = PersonEntity.builder()
                .id(5L)
                .firstName("Old")
                .lastName("Name")
                .nickName("OldNick")
                .job(JobEnum.ACTOR)
                .build();

        final var update = PersonDTO.builder()
                .id(999L)
                .firstName("New")
                .lastName("Person")
                .nickName("NewNick")
                .job(JobEnum.REALISATOR)
                .build();

        mapper.updateEntity(entity, update);

        assertThat(entity.getId()).isEqualTo(5L);
        assertThat(entity.getFirstName()).isEqualTo("New");
        assertThat(entity.getLastName()).isEqualTo("Person");
        assertThat(entity.getNickName()).isEqualTo("NewNick");
        assertThat(entity.getJob()).isEqualTo(JobEnum.REALISATOR);
    }

    @Test
    void shouldHandleNullInputs() {
        final var entity = PersonEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .job(JobEnum.ACTOR)
                .build();

        assertThat(mapper.toEntity(null)).isNull();
        assertThat(mapper.toDto((PersonEntity) null)).isNull();
        assertThat(mapper.toDto((List<PersonEntity>) null)).isNull();

        mapper.updateEntity(entity, null);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getFirstName()).isEqualTo("John");
        assertThat(entity.getLastName()).isEqualTo("Doe");
        assertThat(entity.getJob()).isEqualTo(JobEnum.ACTOR);
    }
}

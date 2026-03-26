package fr.esgi.filmographie.person;

import fr.esgi.filmographie.enums.JobEnum;
import fr.esgi.filmographie.person.exception.MissingPersonNameException;
import fr.esgi.filmographie.exception.NotFoundException;
import fr.esgi.filmographie.person.dto.PersonDTO;
import fr.esgi.filmographie.person.exception.PersonNotFoundException;
import fr.esgi.filmographie.person.mapper.PersonMapper;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @InjectMocks
    PersonService personService;

    @Mock
    PersonRepository personRepository;

    @Mock
    PersonMapper personMapper;

    @Nested
    class GetAllTests {
        @Test
        void shouldReturnAllPersonsAsDTOs(){
            final PersonEntity entity1 = PersonEntity.builder().id(1L).firstName("Toto").lastName("Titi").job(JobEnum.ACTOR).build();
            final PersonEntity entity2 = PersonEntity.builder().id(2L).firstName("Tata").lastName("Titi").job(JobEnum.REALISATOR).build();
            final PersonDTO dto1 = PersonDTO.builder().id(1L).firstName("Toto").lastName("Titi").job(JobEnum.ACTOR).build();
            final PersonDTO dto2 = PersonDTO.builder().id(2L).firstName("Tata").lastName("Titi").job(JobEnum.REALISATOR).build();
            final List<PersonEntity> enties = List.of(entity1, entity2);
            final List<PersonDTO> dtos = List.of(dto1, dto2);

            doReturn(enties).when(personRepository).findAll();
            doReturn(dtos).when(personMapper).toDto(enties);

            final List<PersonDTO> result = personService.getAllPersons();

            assertThat(result).hasSize(2).containsExactly(dto1, dto2);
            verify(personRepository).findAll();
        }
    }
    @Nested
    class GetByIdTests {
        @Test
        void shouldReturnPersonDTOWhenFound() throws NotFoundException {
            final Long id = 1L;
            final PersonEntity entity = PersonEntity.builder().id(id).build();
            final PersonDTO dto = PersonDTO.builder().id(id).build();

            doReturn(Optional.of(entity)).when(personRepository).findById(id);
            doReturn(dto).when(personMapper).toDto(entity);

            final PersonDTO result = personService.getPersonById(id);

            assertThat(result).isEqualTo(dto);
        }

        @Test
        void shouldThrowExceptionWhenNotFound() {
            final var id = 99L;
            doReturn(Optional.empty()).when(personRepository).findById(id);
            assertThatThrownBy(() -> personService.getPersonById(id))
                    .isInstanceOf(PersonNotFoundException.class);
        }
    }

    @Nested
    class CreateTests {
        @Test
        void shouldSaveAndReturnDTO() {
            final PersonDTO inputDto = PersonDTO.builder().id(1L).firstName("Toto").lastName("Titi").job(JobEnum.ACTOR).build();
            final PersonEntity entity = PersonEntity.builder().id(1L).firstName("Toto").lastName("Titi").job(JobEnum.ACTOR).build();
            final PersonDTO outputDto = PersonDTO.builder().id(1L).firstName("Toto").lastName("Titi").job(JobEnum.ACTOR).build();

            doReturn(entity).when(personMapper).toEntity(inputDto);
            doReturn(entity).when(personRepository).save(entity);
            doReturn(outputDto).when(personMapper).toDto(entity);

            final PersonDTO result = personService.createPerson(inputDto);

            assertThat(result).isEqualTo(outputDto);
            verify(personRepository).save(entity);
        }

        @Test
        void shouldThrowMissingPersonNameExceptionWhenPersonDontHaveLastNameOrNickName(){
            final PersonDTO inputDto = PersonDTO.builder().id(1L).firstName("Toto").job(JobEnum.ACTOR).build();
            assertThatThrownBy(() -> personService.createPerson(inputDto))
                    .isInstanceOf(MissingPersonNameException.class);

        }
        @Test
        void shouldThrowMissingPersonNameExceptionWhenPersonDontHaveFistNameOrNickName(){
            final PersonDTO inputDto = PersonDTO.builder().id(1L).lastName("Toto").job(JobEnum.ACTOR).build();
            assertThatThrownBy(() -> personService.createPerson(inputDto))
                    .isInstanceOf(MissingPersonNameException.class);
        }
        @Test
        void shouldNotThrowMissingPersonNameExceptionWhenPersonHaveLastNameAndFirstName(){
            final PersonDTO inputDto = PersonDTO.builder().id(1L).firstName("Toto").lastName("Titi").job(JobEnum.ACTOR).build();
            final PersonEntity entity = PersonEntity.builder().id(1L).firstName("Toto").lastName("Titi").job(JobEnum.ACTOR).build();
            final PersonDTO outputDto = PersonDTO.builder().id(1L).firstName("Toto").lastName("Titi").job(JobEnum.ACTOR).build();

            doReturn(entity).when(personMapper).toEntity(inputDto);
            doReturn(entity).when(personRepository).save(entity);
            doReturn(outputDto).when(personMapper).toDto(entity);

            assertThatCode(() -> personService.createPerson(inputDto))
                    .doesNotThrowAnyException();
        }
        @Test
        void shouldNotThrowMissingPersonNameExceptionWhenPersonHaveNickName(){
            final PersonDTO inputDto = PersonDTO.builder().id(1L).nickName("Toto").job(JobEnum.ACTOR).build();
            final PersonEntity entity = PersonEntity.builder().id(1L).nickName("Toto").job(JobEnum.ACTOR).build();
            final PersonDTO outputDto = PersonDTO.builder().id(1L).nickName("Toto").job(JobEnum.ACTOR).build();

            doReturn(entity).when(personMapper).toEntity(inputDto);
            doReturn(entity).when(personRepository).save(entity);
            doReturn(outputDto).when(personMapper).toDto(entity);

            assertThatCode(() -> personService.createPerson(inputDto))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    class UpdateTests {
        @Test
        void shouldUpdateExistingPerson() throws NotFoundException {
            final Long id = 1L;
            final PersonDTO updateInfo = PersonDTO.builder().id(id).firstName("Toto").lastName("Titi").job(JobEnum.ACTOR).build();
            final PersonEntity existingPerson = PersonEntity.builder().id(id).firstName("Toto").lastName("Titi").job(JobEnum.ACTOR).build();
            final PersonDTO updatedDto = PersonDTO.builder().id(id).firstName("Toto").lastName("Titi").job(JobEnum.REALISATOR).build();

            doReturn(true).when(personRepository).existsById(id);
            doReturn(existingPerson).when(personMapper).toEntity(updateInfo);
            doReturn(existingPerson).when(personRepository).save(existingPerson);
            doReturn(updatedDto).when(personMapper).toDto(existingPerson);

            final PersonDTO result = personService.updatePerson(updateInfo);

            assertThat(result.getJob()).isEqualTo(JobEnum.REALISATOR);
            verify(personRepository).save(existingPerson);
        }

        @Test
        void shouldThrowMissingPersonNameExceptionWhenPersonDontHaveLastNameOrNickName(){
            final PersonDTO inputDto = PersonDTO.builder().id(1L).firstName("Toto").job(JobEnum.ACTOR).build();
            assertThatThrownBy(() -> personService.updatePerson(inputDto))
                    .isInstanceOf(MissingPersonNameException.class);

        }
        @Test
        void shouldThrowMissingPersonNameExceptionWhenPersonDontHaveFistNameOrNickName(){
            final PersonDTO inputDto = PersonDTO.builder().id(1L).lastName("Toto").job(JobEnum.ACTOR).build();
            assertThatThrownBy(() -> personService.updatePerson(inputDto))
                    .isInstanceOf(MissingPersonNameException.class);
        }
        @Test
        void shouldNotThrowMissingPersonNameExceptionWhenPersonHaveLastNameAndFirstName(){
            final Long id = 1L;
            final PersonDTO updateInfo = PersonDTO.builder().id(id).firstName("Toto").lastName("Titi").job(JobEnum.ACTOR).build();
            final PersonEntity existingPerson = PersonEntity.builder().id(id).firstName("Toto").lastName("Titi").job(JobEnum.ACTOR).build();
            final PersonDTO updatedDto = PersonDTO.builder().id(id).firstName("Toto").lastName("Titi").job(JobEnum.ACTOR).build();

            doReturn(true).when(personRepository).existsById(id);
            doReturn(existingPerson).when(personMapper).toEntity(updateInfo);
            doReturn(existingPerson).when(personRepository).save(existingPerson);
            doReturn(updatedDto).when(personMapper).toDto(existingPerson);

            assertThatCode(() -> personService.updatePerson(updateInfo))
                    .doesNotThrowAnyException();
        }
        @Test
        void shouldNotThrowMissingPersonNameExceptionWhenPersonHaveNickName(){
            final Long id = 1L;
            final PersonDTO updateInfo = PersonDTO.builder().id(id).nickName("Toto").job(JobEnum.ACTOR).build();
            final PersonEntity existingPerson = PersonEntity.builder().id(id).nickName("Toto").job(JobEnum.ACTOR).build();
            final PersonDTO updatedDto = PersonDTO.builder().id(id).nickName("Toto").job(JobEnum.ACTOR).build();

            doReturn(true).when(personRepository).existsById(id);
            doReturn(existingPerson).when(personMapper).toEntity(updateInfo);
            doReturn(existingPerson).when(personRepository).save(existingPerson);
            doReturn(updatedDto).when(personMapper).toDto(existingPerson);

            assertThatCode(() -> personService.updatePerson(updateInfo))
                    .doesNotThrowAnyException();
        }

        @Test
        void shouldThrowPersonNotFoundException(){
            final PersonDTO updateInfo = PersonDTO.builder().id(1L).nickName("Toto").job(JobEnum.ACTOR).build();
            doReturn(false).when(personRepository).existsById(1L);
            assertThatThrownBy(() -> personService.updatePerson(updateInfo))
                    .isInstanceOf(PersonNotFoundException.class);
        }

    }

    @Nested
    @DisplayName("delete tests")
    class DeleteTests {
        @Test
        void shouldDeleteWhenExists(){
            final var id = 1L;
            doReturn(true).when(personRepository).existsById(id);

            personService.deletePerson(id);

            verify(personRepository).deleteById(id);
        }

        @Test
        void shouldThrowExceptionWhenDeletingNonExistent() {
            final var id = 1L;
            doReturn(false).when(personRepository).existsById(id);

            assertThatThrownBy(() -> personService.deletePerson(id))
                    .isInstanceOf(PersonNotFoundException.class);

            verify(personRepository, never()).deleteById(anyLong());
        }
    }
}

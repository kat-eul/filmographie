package fr.esgi.filmographie.person;

import fr.esgi.filmographie.enums.JobEnum;
import fr.esgi.filmographie.person.exception.MissingPersonNameException;
import fr.esgi.filmographie.person.dto.PersonDTO;
import fr.esgi.filmographie.person.exception.PersonNotFoundException;
import fr.esgi.filmographie.person.mapper.PersonMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public PersonDTO createPerson(PersonDTO personDTO){
        personGetNickNameOrFirstNameAndLastName(personDTO);
        PersonEntity personEntity = personMapper.toEntity(personDTO);
        personRepository.save(personEntity);
        return personMapper.toDto(personEntity);
    }

    public PersonDTO updatePerson(Long id,PersonDTO personDTO){
        personDTO.setId(id);
        personGetNickNameOrFirstNameAndLastName(personDTO);
        personExist(personDTO.getId());
        PersonEntity personEntity = personMapper.toEntity(personDTO);
        personRepository.save(personEntity);
        return personMapper.toDto(personEntity);
    }

    public PersonDTO getPersonById(Long id){
        PersonEntity personEntity = personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
        return personMapper.toDto(personEntity);
    }

    public List<PersonDTO> getAllPersons(){
        List<PersonEntity> personEntities = personRepository.findAll();
        return personMapper.toDto(personEntities);
    }

    public List<PersonDTO> getAllPersonsByJob(JobEnum job){
        List<PersonEntity> personEntities = personRepository.findAllByJobIn(job.includedForFilter());
        return personMapper.toDto(personEntities);
    }

    public void deletePerson(Long id){
        personExist(id);
        personRepository.deleteById(id);
    }

    private void personGetNickNameOrFirstNameAndLastName(PersonDTO personDTO){
        if (!checkPersonGetNickNameOrFirstNameAndLastName(personDTO)){
            throw new MissingPersonNameException();
        }
    }

    private boolean checkPersonGetNickNameOrFirstNameAndLastName(PersonDTO personDTO){
        return (hasText(personDTO.getFirstName()) && hasText(personDTO.getLastName()))
                || hasText(personDTO.getNickName());
    }
    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private void personExist(Long id){
        boolean personNonExistant = !this.personRepository.existsById(id);
        if (personNonExistant) {
            throw new PersonNotFoundException(id);
        }
    }
}

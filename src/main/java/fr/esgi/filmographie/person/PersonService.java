package fr.esgi.filmographie.person;

import fr.esgi.filmographie.exception.MissingPersonNameException;
import fr.esgi.filmographie.person.dto.PersonDTO;
import fr.esgi.filmographie.person.exception.PersonNotFoundException;
import fr.esgi.filmographie.person.mapper.PersonMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public PersonDTO updatePerson(PersonDTO personDTO){
        personGetNickNameOrFirstNameAndLastName(personDTO);
        PersonEntity personEntity = personRepository.findById(personDTO.getId())
                .orElseThrow(() -> new PersonNotFoundException(personDTO.getId()));
        personMapper.updateEntity(personEntity,personDTO);
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

    public void deletePerson(Long id){
        boolean personNonExistant = !this.personRepository.existsById(id);
        if (personNonExistant) {
            throw new PersonNotFoundException(id);
        }
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
}

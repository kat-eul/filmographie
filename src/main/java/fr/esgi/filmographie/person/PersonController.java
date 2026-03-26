package fr.esgi.filmographie.person;

import fr.esgi.filmographie.person.dto.PersonDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("v1/persons")
@AllArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PersonDTO> getAllPersons(){
        return personService.getAllPersons();
    }

    @GetMapping("/{personId}")
    @ResponseStatus(HttpStatus.OK)
    public PersonDTO getPersonById(@PathVariable Long personId){
        return personService.getPersonById(personId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonDTO createPerson(@Valid @RequestBody PersonDTO personDTO){
        return personService.createPerson(personDTO);
    }

    @PutMapping("/{personId}")
    @ResponseStatus(HttpStatus.OK)
    public PersonDTO updatePerson(@PathVariable Long personId, @Valid @RequestBody PersonDTO personDTO){
        return personService.updatePerson(personId,personDTO);
    }

    @DeleteMapping("/{personId}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePerson(@PathVariable Long personId){
        personService.deletePerson(personId);
    }
}

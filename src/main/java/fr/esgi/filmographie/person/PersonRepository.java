package fr.esgi.filmographie.person;

import org.springframework.data.repository.ListCrudRepository;

public interface PersonRepository extends ListCrudRepository<PersonEntity,Long> {
}

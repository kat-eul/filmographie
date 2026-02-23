package fr.esgi.filmographie.repository;

import fr.esgi.filmographie.entity.PersonEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface PersonRepository extends ListCrudRepository<PersonEntity,Long> {
}

package fr.esgi.filmographie.person;

import fr.esgi.filmographie.enums.JobEnum;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Collection;
import java.util.List;

public interface PersonRepository extends ListCrudRepository<PersonEntity,Long> {
    List<PersonEntity> findAllByJobIn(Collection<JobEnum> job);
}

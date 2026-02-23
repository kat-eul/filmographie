package fr.esgi.filmographie.repository;

import fr.esgi.filmographie.entity.MovieEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface MovieRepository extends ListCrudRepository<MovieEntity, Long> {
}

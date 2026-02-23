package fr.esgi.filmographie.movie;

import org.springframework.data.repository.ListCrudRepository;

public interface MovieRepository extends ListCrudRepository<MovieEntity, Long> {
}

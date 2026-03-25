package fr.esgi.filmographie.genre;

import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public interface GenreRepository extends ListCrudRepository<GenreEntity, Long> {
}

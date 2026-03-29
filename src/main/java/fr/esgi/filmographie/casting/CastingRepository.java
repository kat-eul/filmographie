package fr.esgi.filmographie.casting;

import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface CastingRepository extends ListCrudRepository<CastingEntity, Long> {
    Optional<CastingEntity> findById(CastingId castingId);
    List<CastingEntity> findByMovieId(Long movieId);
    List<CastingEntity> findByRoleId(Long roleId);
    List<CastingEntity> findByActorId(Long personId);
}

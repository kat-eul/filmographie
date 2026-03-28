package fr.esgi.filmographie.casting;

import fr.esgi.filmographie.movie.MovieEntity;
import fr.esgi.filmographie.person.PersonEntity;
import fr.esgi.filmographie.role.RoleEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "movies")
public class CastingEntity {
    @EmbeddedId
    private CastingId id;

    @MapsId("movieId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private MovieEntity movie;

    @MapsId("roleId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

    @MapsId("actorId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "actor_id", nullable = false)
    private PersonEntity actor;
}



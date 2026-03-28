package fr.esgi.filmographie.movie_role_actor;

import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class MovieRoleActorId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "movie_id")
    private Long movieId;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "actor_id")
    private Long actorId;
}

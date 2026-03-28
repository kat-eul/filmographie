package fr.esgi.filmographie.casting;

import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class CastingId implements Serializable {

    @Column(name = "movie_id")
    private Long movieId;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "actor_id")
    private Long actorId;
}

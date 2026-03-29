package fr.esgi.filmographie.casting;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

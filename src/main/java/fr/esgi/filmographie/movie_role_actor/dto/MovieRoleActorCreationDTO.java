package fr.esgi.filmographie.movie_role_actor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieRoleActorCreationDTO {

    @NotBlank
    private Long movieId;

    @NotBlank
    private Long roleId;

    private Long actorId;
}

package fr.esgi.filmographie.casting.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CastingDTO {
    @NotNull
    private Long movieId;
    private String movieTitle;

    @NotNull
    private Long roleId;
    private String roleName;

    @NotNull
    private Long actorId;
    private String actorName;

    public CastingDTO(Long movieId, Long roleId, Long actorId) {
        this.movieId = movieId;
        this.roleId = roleId;
        this.actorId = actorId;
    }
}

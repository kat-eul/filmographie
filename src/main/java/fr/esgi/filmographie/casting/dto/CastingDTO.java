package fr.esgi.filmographie.casting.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CastingDTO {
    private Long movieId;
    private String movieTitle;

    private Long roleId;
    private String roleName;

    private Long actorId;
    private String actorName;
}

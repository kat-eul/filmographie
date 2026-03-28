package fr.esgi.filmographie.casting.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CastingCreationDTO {

    @NotBlank
    private Long movieId;
    @NotBlank
    private Long roleId;
    @NotBlank
    private Long actorId;
}

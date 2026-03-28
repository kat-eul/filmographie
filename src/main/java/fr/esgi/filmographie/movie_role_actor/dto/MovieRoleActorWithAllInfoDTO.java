package fr.esgi.filmographie.movie_role_actor.dto;

import fr.esgi.filmographie.movie.dto.MovieDTO;
import fr.esgi.filmographie.person.dto.PersonDTO;
import fr.esgi.filmographie.role.dto.RoleDTO;
import jakarta.validation.constraints.NotBlank;

public class MovieRoleActorWithAllInfoDTO {

    @NotBlank
    private Long id;

    private MovieDTO movie;
    private RoleDTO role;
    private PersonDTO actor;
}

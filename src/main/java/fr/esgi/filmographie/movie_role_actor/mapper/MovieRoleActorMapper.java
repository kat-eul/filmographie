package fr.esgi.filmographie.movie_role_actor.mapper;

import fr.esgi.filmographie.movie.MovieRepository;
import fr.esgi.filmographie.movie_role_actor.MovieRoleActorEntity;
import fr.esgi.filmographie.movie_role_actor.MovieRoleActorId;
import fr.esgi.filmographie.movie_role_actor.dto.MovieRoleActorCreationDTO;
import fr.esgi.filmographie.movie_role_actor.dto.MovieRoleActorWithAllInfoDTO;
import fr.esgi.filmographie.person.PersonRepository;
import fr.esgi.filmographie.role.RoleRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
@AllArgsConstructor
public abstract class MovieRoleActorMapper {

    private final MovieRepository movieRepository;
    private final RoleRepository roleRepository;
    private final PersonRepository personRepository;

    // Map entity -> DTO en exposant les ids des relations
    @Mapping(source = "movie.id", target = "movieId")
    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "actor.id", target = "actorId")
    public abstract MovieRoleActorWithAllInfoDTO entityToDto(final MovieRoleActorEntity entity);

    // DTO création -> Entity : ignorer id et relations pour les remplir dans @AfterMapping
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "actor", ignore = true)
    public abstract MovieRoleActorEntity dtoToEntity(final MovieRoleActorCreationDTO dto);

    // Update partiel : ne pas toucher à l'id et aux relations automatiques
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "actor", ignore = true)
    public abstract void updateEntity(@MappingTarget MovieRoleActorEntity entity, final MovieRoleActorCreationDTO dto);


    @AfterMapping
    protected void fillEntityFromDto(final MovieRoleActorCreationDTO dto, @MappingTarget MovieRoleActorEntity entity) {
        if (dto == null) {
            return;
        }

        if (dto.getMovieId() != null) {
            entity.setMovie(movieRepository.getReferenceById(dto.getMovieId()));
        } else {
            entity.setMovie(null);
        }

        if (dto.getRoleId() != null) {
            entity.setRole(roleRepository.getReferenceById(dto.getRoleId()));
        } else {
            entity.setRole(null);
        }

        if (dto.getActorId() != null) {
            entity.setActor(personRepository.getReferenceById(dto.getActorId()));
        } else {
            entity.setActor(null);
        }
    }
}

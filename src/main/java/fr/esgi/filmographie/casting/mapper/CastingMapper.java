package fr.esgi.filmographie.casting.mapper;

import fr.esgi.filmographie.casting.CastingEntity;
import fr.esgi.filmographie.casting.dto.CastingCreationDTO;
import fr.esgi.filmographie.casting.dto.CastingDTO;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
@AllArgsConstructor
public abstract class CastingMapper {

    @Mapping(target = "id.movieId", source = "movieId")
    @Mapping(target = "id.roleId", source = "roleId")
    @Mapping(target = "id.actorId", source = "actorId")
    public abstract CastingEntity toEntity(CastingCreationDTO dto);

    @Mapping(target = "movieId", source = "id.movieId")
    @Mapping(target = "movieTitle", source = "movie.title")
    @Mapping(target = "roleId", source = "id.roleId")
    @Mapping(target = "roleName", source = "role.name")
    @Mapping(target = "actorId", source = "id.actorId")
    @Mapping(target = "actorName", source = "actor.nickName")
    public abstract CastingDTO toDTO(CastingEntity entity);
}

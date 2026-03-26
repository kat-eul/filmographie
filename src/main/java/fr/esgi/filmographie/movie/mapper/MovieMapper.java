package fr.esgi.filmographie.movie.mapper;

import fr.esgi.filmographie.movie.MovieEntity;
import fr.esgi.filmographie.movie.dto.MovieDTO;
import fr.esgi.filmographie.movie.dto.MovieWithAllInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    MovieDTO entityToDto(final MovieEntity movieEntity);

    @Mapping(target = "genres", ignore = true)
    MovieEntity dtoToEntity(final MovieDTO movieDTO);

    MovieWithAllInfoDTO entityToWithAllInfoDto(MovieEntity movieEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "genres", ignore = true)
    void updateEntity(@MappingTarget MovieEntity movieEntity, final MovieDTO movieDTO);
}

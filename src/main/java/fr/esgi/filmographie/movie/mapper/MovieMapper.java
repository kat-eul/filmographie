package fr.esgi.filmographie.movie.mapper;

import fr.esgi.filmographie.movie.MovieEntity;
import fr.esgi.filmographie.movie.dto.MovieDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    MovieDTO entityToDto(final MovieEntity movieEntity);
    MovieEntity dtoToEntity(final MovieDTO movieDTO);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget MovieEntity movieEntity, final MovieDTO movieDTO);
}

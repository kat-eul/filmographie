package fr.esgi.filmographie.mapper;

import fr.esgi.filmographie.dto.MovieDTO;
import fr.esgi.filmographie.entity.MovieEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface MovieMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "releaseDate", source = "releaseDate")
    @Mapping(target = "summary", source = "summary")
    MovieDTO entityToDto(final MovieEntity movieEntity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "releaseDate", source = "releaseDate")
    @Mapping(target = "summary", source = "summary")
    MovieEntity dtoToEntity(final MovieDTO movieDTO);
}

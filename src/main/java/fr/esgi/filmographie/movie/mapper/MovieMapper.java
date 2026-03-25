package fr.esgi.filmographie.movie.mapper;

import fr.esgi.filmographie.movie.MovieEntity;
import fr.esgi.filmographie.movie.dto.MovieDTO;
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

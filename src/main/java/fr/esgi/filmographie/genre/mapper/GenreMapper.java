package fr.esgi.filmographie.genre.mapper;

import fr.esgi.filmographie.genre.GenreEntity;
import fr.esgi.filmographie.genre.dto.GenreDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreEntity dtoToEntity(final GenreDTO genreDTO);

    GenreDTO entityToDto(final GenreEntity genreEntity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget GenreEntity genreEntity, GenreDTO genreDTO);
}

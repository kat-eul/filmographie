package fr.esgi.filmographie.person.mapper;

import fr.esgi.filmographie.person.PersonEntity;
import fr.esgi.filmographie.person.dto.PersonDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    PersonEntity toEntity(PersonDTO personDTO);
    PersonDTO toDto(PersonEntity personEntity);
    List<PersonDTO> toDto(List<PersonEntity> personEntities);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget PersonEntity personEntity, PersonDTO personDTO);
}

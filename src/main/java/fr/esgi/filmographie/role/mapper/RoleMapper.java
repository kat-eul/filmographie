package fr.esgi.filmographie.role.mapper;

import fr.esgi.filmographie.role.RoleEntity;
import fr.esgi.filmographie.role.dto.RoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {
        RoleDTO entityToDto(final RoleEntity roleEntity);
        RoleEntity dtoToEntity(final RoleDTO roleDTO);

        @Mapping(target = "id", ignore = true)
        void updateEntity(@MappingTarget RoleEntity roleEntity, final RoleDTO roleDTO);
}

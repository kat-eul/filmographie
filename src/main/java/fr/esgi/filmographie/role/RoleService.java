package fr.esgi.filmographie.role;

import fr.esgi.filmographie.role.dto.RoleDTO;
import fr.esgi.filmographie.role.exception.RoleNotFoundException;
import fr.esgi.filmographie.role.mapper.RoleMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleDTO create(final RoleDTO roleDTO) {
        final RoleEntity roleEntity = roleMapper.dtoToEntity(roleDTO);
        final RoleEntity savedRoleEntity = roleRepository.save(roleEntity);
        return roleMapper.entityToDto(savedRoleEntity);
    }

    public java.util.List<RoleDTO> getAll() {
        return roleRepository.findAll().stream()
                .map(roleMapper::entityToDto)
                .toList();
    }

    public RoleDTO getById(final Long id) throws RoleNotFoundException {
        final RoleEntity roleEntity = roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(id));
        return roleMapper.entityToDto(roleEntity);
    }

    public RoleDTO update(final Long id, final RoleDTO roleDTO) throws RoleNotFoundException {
        final RoleEntity roleEntity = roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(id));
        roleMapper.updateEntity(roleEntity, roleDTO);
        final RoleEntity updatedRoleEntity = roleRepository.save(roleEntity);
        return roleMapper.entityToDto(updatedRoleEntity);
    }

    public void delete(final Long id) throws RoleNotFoundException {
        if (!roleRepository.existsById(id)) {
            throw new RoleNotFoundException(id);
        }
        roleRepository.deleteById(id);
    }
}

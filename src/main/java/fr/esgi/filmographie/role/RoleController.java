package fr.esgi.filmographie.role;

import fr.esgi.filmographie.role.dto.RoleDTO;
import fr.esgi.filmographie.role.exception.RoleNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/roles")
@AllArgsConstructor
public class RoleController {
        private final RoleService roleService;

        @GetMapping()
        @ResponseStatus(HttpStatus.OK)
        public Iterable<RoleDTO> getAll() {
            return this.roleService.getAll();
        }

        @GetMapping("/{id}")
        @ResponseStatus(HttpStatus.OK)
        public RoleDTO getById(@PathVariable Long id) throws RoleNotFoundException {
            return this.roleService.getById(id);
        }

        @PostMapping()
        @ResponseStatus(HttpStatus.CREATED)
        public RoleDTO create(@RequestBody RoleDTO roleDTO) {
            return this.roleService.create(roleDTO);
        }

        @PutMapping("/{id}")
        @ResponseStatus(HttpStatus.OK)
        public RoleDTO update(@PathVariable Long id, @RequestBody RoleDTO roleDTO) throws RoleNotFoundException {
            return this.roleService.update(id, roleDTO);
        }

        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void delete(@PathVariable Long id) throws RoleNotFoundException {
            this.roleService.delete(id);
        }
}

package fr.esgi.filmographie.casting;

import fr.esgi.filmographie.casting.dto.CastingDTO;
import fr.esgi.filmographie.exception.NotFoundException;
import fr.esgi.filmographie.casting.dto.CastingCreationDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/castings")
@AllArgsConstructor
public class CastingController {
    private final CastingService movieService;

    @PostMapping("/movies/{movieId}/roles/{roleId}/actors/{actorId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CastingDTO create(@PathVariable Long movieId, @PathVariable Long roleId, @PathVariable Long actorId) {
        CastingCreationDTO dto = new CastingCreationDTO(movieId, roleId, actorId);
        return movieService.create(dto);
    }

    @GetMapping("/movies/{movieId}/roles/{roleId}/actors/{actorId}")
    public CastingDTO getById(@PathVariable Long movieId, @PathVariable Long roleId, @PathVariable Long actorId) {
        return movieService.getById(movieId, roleId, actorId);
    }

    @GetMapping
    public List<CastingDTO> getAll() {
        return movieService.getAll();
    }

    @GetMapping("/movies/{movieId}")
    public List<CastingDTO> getByMovieId(@PathVariable Long movieId) {
        return movieService.getByMovieId(movieId);
    }

    @GetMapping("/roles/{roleId}")
    public List<CastingDTO> getByRoleId(@PathVariable Long roleId) {
        return movieService.getByRoleId(roleId);
    }

    @GetMapping("/actors/{actorId}")
    public List<CastingDTO> getByActorId(@PathVariable Long actorId) {
        return movieService.getByActorId(actorId);
    }

    @PutMapping("/movies/{movieId}/roles/{roleId}/actors/{actorId}")
    public CastingDTO update(@PathVariable Long movieId, @PathVariable Long roleId, @PathVariable Long actorId, @Valid @RequestBody CastingCreationDTO dto) {
        if (!movieId.equals(dto.getMovieId()) || !roleId.equals(dto.getRoleId()) || !actorId.equals(dto.getActorId())) {
            throw new NotFoundException("Casting not found with movieId: " + movieId + ", roleId: " + roleId + ", actorId: " + actorId);
        }
        return movieService.create(dto);
    }

    @DeleteMapping("/movies/{movieId}/roles/{roleId}/actors/{actorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long movieId, @PathVariable Long roleId, @PathVariable Long actorId) {
        movieService.delete(movieId, roleId, actorId);
    }
}

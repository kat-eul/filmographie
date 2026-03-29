package fr.esgi.filmographie.casting;

import fr.esgi.filmographie.casting.dto.CastingDTO;
import fr.esgi.filmographie.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/castings")
@AllArgsConstructor
public class CastingController {
    private final CastingService castingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CastingDTO create(@Valid @RequestBody CastingDTO dto) {
        return castingService.create(dto);
    }

    @GetMapping("/movies/{movieId}/roles/{roleId}/actors/{actorId}")
    public CastingDTO getById(@PathVariable Long movieId, @PathVariable Long roleId, @PathVariable Long actorId) {
        return castingService.getById(movieId, roleId, actorId);
    }

    @GetMapping
    public List<CastingDTO> getAll() {
        return castingService.getAll();
    }

    @GetMapping("/movies/{movieId}")
    public List<CastingDTO> getByMovieId(@PathVariable Long movieId) {
        return castingService.getByMovieId(movieId);
    }

    @GetMapping("/roles/{roleId}")
    public List<CastingDTO> getByRoleId(@PathVariable Long roleId) {
        return castingService.getByRoleId(roleId);
    }

    @GetMapping("/actors/{actorId}")
    public List<CastingDTO> getByActorId(@PathVariable Long actorId) {
        return castingService.getByActorId(actorId);
    }

    @PutMapping("/movies/{movieId}/roles/{roleId}/actors/{actorId}")
    public CastingDTO update(@PathVariable Long movieId, @PathVariable Long roleId, @PathVariable Long actorId, @Valid @RequestBody CastingDTO dto) {
        if (!movieId.equals(dto.getMovieId()) || !roleId.equals(dto.getRoleId()) || !actorId.equals(dto.getActorId())) {
            throw new NotFoundException("Casting not found with movieId: " + movieId + ", roleId: " + roleId + ", actorId: " + actorId);
        }
        return castingService.create(dto);
    }

    @DeleteMapping("/movies/{movieId}/roles/{roleId}/actors/{actorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long movieId, @PathVariable Long roleId, @PathVariable Long actorId) {
        castingService.delete(movieId, roleId, actorId);
    }
}

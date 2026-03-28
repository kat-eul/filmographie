package fr.esgi.filmographie.movie_role_actor;

import fr.esgi.filmographie.exception.NotFoundException;
import fr.esgi.filmographie.movie_role_actor.dto.MovieRoleActorCreationDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/movies")
@AllArgsConstructor
public class MovieRoleActorController {
    private final MovieRoleActorService movieService;

    @GetMapping(path = "/")
    @ResponseStatus(HttpStatus.OK)
    public List<MovieRoleActorCreationDTO> getMovieById() {
        return this.movieService.getAll();
    }

    @GetMapping(path = "/{movieId}")
    @ResponseStatus(HttpStatus.OK)
    public MovieRoleActorCreationDTO getMovieById(@PathVariable Long movieId) throws NotFoundException {
        return this.movieService.getById(movieId);
    }

    @PostMapping(path = "/")
    @ResponseStatus(HttpStatus.CREATED)
    public MovieRoleActorCreationDTO createMovie(@Valid @RequestBody MovieRoleActorCreationDTO movieDTO) {
        return this.movieService.create(movieDTO);
    }

    @PutMapping(path = "/{movieId}")
    @ResponseStatus(HttpStatus.OK)
    public MovieRoleActorCreationDTO updateMovie(@PathVariable Long movieId, @Valid @RequestBody MovieRoleActorCreationDTO movieDTO) throws NotFoundException {
        return this.movieService.update(movieId, movieDTO);
    }

    @DeleteMapping(path = "/{movieId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable Long movieId) throws NotFoundException {
        this.movieService.delete(movieId);
    }
}

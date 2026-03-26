package fr.esgi.filmographie.movie;

import fr.esgi.filmographie.exception.NotFoundException;
import fr.esgi.filmographie.movie.dto.MovieDTO;
import fr.esgi.filmographie.movie.dto.MovieWithAllInfoDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(final MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MovieDTO> getMovieById() {
        return this.movieService.getAll();
    }

    @GetMapping(path = "/{movieId}")
    @ResponseStatus(HttpStatus.OK)
    public MovieWithAllInfoDTO getMovieById(@PathVariable Long movieId) throws NotFoundException {
        return this.movieService.getById(movieId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieDTO createMovie(@Valid @RequestBody MovieDTO movieDTO) {
        return this.movieService.create(movieDTO);
    }

    @PostMapping(path = "/{movieId}/genres/{genreId}")
    @ResponseStatus(HttpStatus.OK)
    public MovieWithAllInfoDTO addGenreToMovie(@PathVariable Long movieId, @PathVariable Long genreId) throws NotFoundException {
        return this.movieService.addGenreToMovie(movieId, genreId);
    }

    @PutMapping(path = "/{movieId}")
    @ResponseStatus(HttpStatus.OK)
    public MovieDTO updateMovie(@PathVariable Long movieId, @Valid @RequestBody MovieDTO movieDTO) throws NotFoundException {
        return this.movieService.update(movieId, movieDTO);
    }

    @DeleteMapping(path = "/{movieId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable Long movieId) throws NotFoundException {
        this.movieService.delete(movieId);
    }
}

package fr.esgi.filmographie.movie;

import fr.esgi.filmographie.movie.dto.MovieDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(final MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public MovieDTO createMovie(@Valid @RequestBody MovieDTO movieDTO) {
        return this.movieService.create(movieDTO);
    }

    @PostMapping()
    public MovieDTO getMovie(@Valid @RequestBody MovieDTO movieDTO) {
        return this.movieService.create(movieDTO);
    }
}

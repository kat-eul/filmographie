package fr.esgi.filmographie.genre;

import fr.esgi.filmographie.genre.dto.GenreDTO;
import fr.esgi.filmographie.genre.exception.GenreNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
@RequestMapping("/v1/genres")
@AllArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GenreDTO> getAll() {
        return this.genreService.getAll();
    }

    @GetMapping("/{genreId}")
    @ResponseStatus(HttpStatus.OK)
    public GenreDTO getById(@PathVariable final Long genreId) throws GenreNotFoundException {
        return this.genreService.getById(genreId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenreDTO create(@Valid @RequestBody final GenreDTO genreDTO) {
        return this.genreService.create(genreDTO);
    }

    @PutMapping("/{genreId}")
    @ResponseStatus(HttpStatus.OK)
    public GenreDTO update(@PathVariable final Long genreId, @Valid @RequestBody final GenreDTO genreDTO) throws GenreNotFoundException {
        return this.genreService.update(genreId, genreDTO);
    }

    @DeleteMapping("/{genreId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final Long genreId) throws GenreNotFoundException {
        this.genreService.delete(genreId);
    }
}

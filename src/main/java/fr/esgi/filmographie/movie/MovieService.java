package fr.esgi.filmographie.movie;

import fr.esgi.filmographie.exception.NotFoundException;
import fr.esgi.filmographie.genre.GenreRepository;
import fr.esgi.filmographie.genre.exception.GenreNotFoundException;
import fr.esgi.filmographie.movie.dto.MovieDTO;
import fr.esgi.filmographie.movie.dto.MovieWithAllInfoDTO;
import fr.esgi.filmographie.movie.exception.MovieNotFoundException;
import fr.esgi.filmographie.movie.mapper.MovieMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieMapper movieMapper;

    public List<MovieDTO> getAll() {
        return this.movieRepository.findAll().stream()
                .map(this.movieMapper::entityToDto)
                .toList();
    }

    public MovieWithAllInfoDTO getById(final Long id) throws NotFoundException {
        return this.movieRepository.findById(id)
                .map(this.movieMapper::entityToWithAllInfoDto)
                .orElseThrow(() -> new MovieNotFoundException(id));
    }

    public MovieDTO create(final MovieDTO movieDTO) {
        final var movieEntity = this.movieMapper.dtoToEntity(movieDTO);

        final var createdMovieEntity = this.movieRepository.save(movieEntity);

        return this.movieMapper.entityToDto(createdMovieEntity);
    }

    public MovieDTO update(final Long id, final MovieDTO movieDTO) throws NotFoundException {
        final var movieEntity = this.movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));

        this.movieMapper.updateEntity(movieEntity, movieDTO);

        final var createdMovieEntity = this.movieRepository.save(movieEntity);

        return this.movieMapper.entityToDto(createdMovieEntity);
    }

    public void delete(final Long id) throws NotFoundException {
        if (this.movieRepository.existsById(id)) {
            this.movieRepository.deleteById(id);
            return;
        }

        throw new MovieNotFoundException(id);
    }

    public MovieWithAllInfoDTO addGenreToMovie(Long movieId, Long genreId) throws MovieNotFoundException, GenreNotFoundException {
        final var movieEntity = this.movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException(movieId));

        final var genreEntity = this.genreRepository.findById(genreId)
                .orElseThrow(() -> new GenreNotFoundException(genreId));

        movieEntity.addGenre(genreEntity);

        final var updatedMovieEntity = this.movieRepository.save(movieEntity);

        return this.movieMapper.entityToWithAllInfoDto(updatedMovieEntity);
    }
}

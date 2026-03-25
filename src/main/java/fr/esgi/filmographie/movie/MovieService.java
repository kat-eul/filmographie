package fr.esgi.filmographie.movie;

import fr.esgi.filmographie.exception.NotFoundException;
import fr.esgi.filmographie.movie.dto.MovieDTO;
import fr.esgi.filmographie.movie.exception.MovieNotFoundException;
import fr.esgi.filmographie.movie.mapper.MovieMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public MovieService(final MovieRepository movieRepository, final MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    public List<MovieDTO> getAll() {
        return this.movieRepository.findAll().stream()
                .map(this.movieMapper::entityToDto)
                .toList();
    }

    public MovieDTO getById(final Long id) throws NotFoundException {
        return this.movieRepository.findById(id)
                .map(this.movieMapper::entityToDto)
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
}

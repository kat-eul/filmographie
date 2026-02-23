package fr.esgi.filmographie.service;

import fr.esgi.filmographie.dto.MovieDTO;
import fr.esgi.filmographie.exception.NotFoundException;
import fr.esgi.filmographie.mapper.MovieMapper;
import fr.esgi.filmographie.repository.MovieRepository;
import org.springframework.stereotype.Service;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public MovieService(final MovieRepository movieRepository, final MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    public MovieDTO getById(final Long id) throws NotFoundException {
        return this.movieRepository.findById(id)
                .map(this.movieMapper::entityToDto)
                .orElseThrow(() -> new NotFoundException("No movie found with id %d".formatted(id)));
    }

    public MovieDTO create(final MovieDTO movieDTO) {
        final var movieEntity = this.movieMapper.dtoToEntity(movieDTO);

        final var createdMovieEntity = this.movieRepository.save(movieEntity);

        return this.movieMapper.entityToDto(createdMovieEntity);
    }

    public MovieDTO update(final MovieDTO movieDTO) {
        final var movieEntity = this.movieMapper.dtoToEntity(movieDTO);

        final var createdMovieEntity = this.movieRepository.save(movieEntity);

        return this.movieMapper.entityToDto(createdMovieEntity);
    }
}

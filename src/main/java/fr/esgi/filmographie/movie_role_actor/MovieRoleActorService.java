package fr.esgi.filmographie.movie_role_actor;

import fr.esgi.filmographie.exception.NotFoundException;
import fr.esgi.filmographie.movie_role_actor.dto.MovieRoleActorCreationDTO;
import fr.esgi.filmographie.movie_role_actor.exception.MovieRoleActorNotFoundException;
import fr.esgi.filmographie.movie_role_actor.mapper.MovieRoleActorMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MovieRoleActorService {
    private final MovieRoleActorRepository movieRoleActorRepository;
    private final MovieRoleActorMapper movieRoleActorMapper;

    public List<MovieRoleActorCreationDTO> getAll() {
        return this.movieRoleActorRepository.findAll().stream()
                .map(this.movieRoleActorMapper::entityToDto)
                .toList();
    }

    public MovieRoleActorCreationDTO getById(final Long id) throws NotFoundException {
        final var movieRoleActorEntity = this.movieRoleActorRepository.findById(id)
                .orElseThrow(() -> new MovieRoleActorNotFoundException(id));
        return this.movieRoleActorMapper.entityToDto(movieRoleActorEntity);
    }

    public MovieRoleActorCreationDTO create(final MovieRoleActorCreationDTO movieRoleActorDTO) {
        final var movieRoleActorEntity = this.movieRoleActorMapper.dtoToEntity(movieRoleActorDTO);
        final var savedMovieRoleActorEntity = this.movieRoleActorRepository.save(movieRoleActorEntity);
        return this.movieRoleActorMapper.entityToDto(savedMovieRoleActorEntity);
    }

    public MovieRoleActorCreationDTO update(final Long id, final MovieRoleActorCreationDTO movieRoleActorDTO) throws NotFoundException {
        final var movieRoleActorEntity = this.movieRoleActorRepository.findById(id)
                .orElseThrow(() -> new MovieRoleActorNotFoundException(id));
        this.movieRoleActorMapper.updateEntity(movieRoleActorEntity, movieRoleActorDTO);
        final var updatedMovieRoleActorEntity = this.movieRoleActorRepository.save(movieRoleActorEntity);
        return this.movieRoleActorMapper.entityToDto(updatedMovieRoleActorEntity);
    }

    public void delete(final Long id) throws NotFoundException {
        final var movieRoleActorEntity = this.movieRoleActorRepository.findById(id)
                .orElseThrow(() -> new MovieRoleActorNotFoundException(id));
        this.movieRoleActorRepository.delete(movieRoleActorEntity);
    }
}

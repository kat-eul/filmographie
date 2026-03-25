package fr.esgi.filmographie.genre;

import fr.esgi.filmographie.genre.dto.GenreDTO;
import fr.esgi.filmographie.genre.exception.GenreNotFoundException;
import fr.esgi.filmographie.genre.mapper.GenreMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    public List<GenreDTO> getAll() {
        return this.genreRepository.findAll().stream()
                .map(genreMapper::entityToDto)
                .toList();
    }

    public GenreDTO getById(Long id) throws GenreNotFoundException {
        return this.genreRepository.findById(id)
                .map(genreMapper::entityToDto)
                .orElseThrow(() -> new GenreNotFoundException(id));
    }

    public GenreDTO create(GenreDTO genreDTO) {
        final var genreEntity = genreMapper.dtoToEntity(genreDTO);
        genreEntity.setId(null);

        final var createdEntity = this.genreRepository.save(genreEntity);

        return this.genreMapper.entityToDto(createdEntity);
    }

    public GenreDTO update(Long id, GenreDTO genreDTO) throws GenreNotFoundException {
        final var genreEntity = this.genreRepository.findById(id)
                .orElseThrow(() -> new GenreNotFoundException(id));

        this.genreMapper.updateEntity(genreEntity, genreDTO);

        final var updatedEntity = this.genreRepository.save(genreEntity);

        return this.genreMapper.entityToDto(updatedEntity);
    }

    public void delete(Long id) throws GenreNotFoundException {
        final var nonExistentGenre = !this.genreRepository.existsById(id);
        if (nonExistentGenre) {
            throw new GenreNotFoundException(id);
        }

        this.genreRepository.deleteById(id);
    }
}

package fr.esgi.filmographie.casting;

import fr.esgi.filmographie.casting.dto.CastingDTO;
import fr.esgi.filmographie.casting.exception.CastingNotFoundException;
import fr.esgi.filmographie.casting.exception.NotAnActorException;
import fr.esgi.filmographie.casting.mapper.CastingMapper;
import fr.esgi.filmographie.enums.JobEnum;
import fr.esgi.filmographie.person.PersonEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CastingService {
    private final CastingRepository castingRepository;
    private final CastingMapper castingMapper;
    private final List<JobEnum> allowedJobs = List.of(JobEnum.ACTOR, JobEnum.REALISATOR_ACTOR);

    public CastingDTO create(CastingDTO dto) {
        CastingEntity entity = castingMapper.toEntity(dto);
        PersonEntity person = entity.getActor();
        validateActor(person);
        CastingEntity savedEntity = castingRepository.save(entity);
        return castingMapper.toDTO(savedEntity);
    }

    public List<CastingDTO> getAll() {
        return castingRepository.findAll().stream().map(castingMapper::toDTO).toList();
    }

    public CastingDTO getById(Long movieId, Long roleId, Long personId) {
        CastingId castingId = new CastingId(movieId, roleId, personId);
        CastingEntity entity = castingRepository.findById(castingId)
                .orElseThrow(() -> new CastingNotFoundException(movieId, roleId, personId));
        return castingMapper.toDTO(entity);
    }

    public List<CastingDTO> getByMovieId(Long movieId) {
        List<CastingEntity> entities = castingRepository.findByMovieId(movieId);
        return entities.stream().map(castingMapper::toDTO).toList();
    }

    public List<CastingDTO> getByRoleId(Long roleId) {
        List<CastingEntity> entities = castingRepository.findByRoleId(roleId);
        return entities.stream().map(castingMapper::toDTO).toList();
    }

    public List<CastingDTO> getByActorId(Long actorId) {
        PersonEntity person = new PersonEntity();
        validateActor(person);
        List<CastingEntity> entities = castingRepository.findByActorId(actorId);
        return entities.stream().map(castingMapper::toDTO).toList();
    }

    public CastingDTO update(Long movieId, Long roleId, Long personId, CastingDTO dto) {
        CastingId castingId = new CastingId(movieId, roleId, personId);
        CastingEntity existing = castingRepository.findById(castingId)
                .orElseThrow(() -> new CastingNotFoundException(movieId, roleId, personId));
        CastingEntity updateEntity = castingMapper.toEntity(dto);

        Optional.ofNullable(updateEntity.getMovie()).ifPresent(existing::setMovie);
        Optional.ofNullable(updateEntity.getRole()).ifPresent(existing::setRole);
        Optional.ofNullable(updateEntity.getActor()).ifPresent(actor -> {
            validateActor(actor);
            existing.setActor(actor);
        });

        return castingMapper.toDTO(castingRepository.save(existing));
    }

    public void delete(Long movieId, Long roleId, Long personId) {
        CastingId castingId = new CastingId(movieId, roleId, personId);
        CastingEntity existing = castingRepository.findById(castingId)
                .orElseThrow(() -> new CastingNotFoundException(movieId, roleId, personId));

        castingRepository.delete(existing);
    }

    private void validateActor(PersonEntity person) {
        if (allowedJobs.contains(person.getJob())) {
            throw new NotAnActorException(person.getFirstName() + " " + person.getLastName());
        }
    }
}

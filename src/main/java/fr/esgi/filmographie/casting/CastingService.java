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
        if(allowedJobs.contains(person.getJob())){
            throw new NotAnActorException(person.getFirstName() + " " + person.getLastName());
        }
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
        if(allowedJobs.contains(person.getJob())){
            throw new NotAnActorException(person.getFirstName() + " " + person.getLastName() + " "+"with job "+person.getJob());
        }
        List<CastingEntity> entities = castingRepository.findByActorId(actorId);
        return entities.stream().map(castingMapper::toDTO).toList();
    }

    public CastingDTO update(Long movieId, Long roleId, Long personId, CastingDTO dto) {
        CastingId castingId = new CastingId(movieId, roleId, personId);
        Optional<CastingEntity> existingEntity =castingRepository.findById(castingId);
        CastingEntity newEntity = castingMapper.toEntity(dto);
        if(existingEntity.isEmpty()){
            throw new CastingNotFoundException(movieId, roleId, personId);
        }

        if(newEntity.getMovie() != null){
             existingEntity.get().setMovie(newEntity.getMovie());
        }
        if(newEntity.getRole() != null){
             existingEntity.get().setRole(newEntity.getRole());
        }
        if(newEntity.getActor() != null){
            PersonEntity person = newEntity.getActor();
            if(allowedJobs.contains(person.getJob())){
                throw new NotAnActorException(person.getFirstName() + " " + person.getLastName());
            }
            existingEntity.get().setActor(newEntity.getActor());
        }
        CastingEntity updatedEntity = castingRepository.save(existingEntity.get());
        return castingMapper.toDTO(updatedEntity);
    }

    public void delete(Long movieId, Long roleId, Long personId) {
        CastingId castingId = new CastingId(movieId, roleId, personId);
        Optional<CastingEntity> existingEntity = castingRepository.findById(castingId);
        if(existingEntity.isEmpty()){
            throw new CastingNotFoundException(movieId, roleId, personId);
        }
        castingRepository.delete(existingEntity.get());
    }
}

package fr.esgi.filmographie.casting;

import fr.esgi.filmographie.casting.dto.CastingDTO;
import fr.esgi.filmographie.casting.exception.CastingNotFoundException;
import fr.esgi.filmographie.casting.exception.NotAnActorException;
import fr.esgi.filmographie.movie.MovieEntity;
import fr.esgi.filmographie.casting.mapper.CastingMapper;
import fr.esgi.filmographie.enums.JobEnum;
import fr.esgi.filmographie.movie.MovieRepository;
import fr.esgi.filmographie.movie.exception.MovieNotFoundException;
import fr.esgi.filmographie.person.PersonEntity;
import fr.esgi.filmographie.person.PersonRepository;
import fr.esgi.filmographie.person.exception.PersonNotFoundException;
import fr.esgi.filmographie.role.RoleEntity;
import fr.esgi.filmographie.role.RoleRepository;
import fr.esgi.filmographie.role.exception.RoleNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CastingService {
    private final CastingRepository castingRepository;
    private final PersonRepository personRepository;
    private final MovieRepository movieRepository;
    private final RoleRepository roleRepository;

    private final CastingMapper castingMapper;
    private final List<JobEnum> allowedJobs = List.of(JobEnum.ACTOR, JobEnum.REALISATOR_ACTOR);

    public CastingDTO create(CastingDTO dto) {
        PersonEntity actor = personRepository.findById(dto.getActorId())
                .orElseThrow(() -> new PersonNotFoundException(dto.getActorId()));

        MovieEntity movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new MovieNotFoundException(dto.getMovieId()));
        RoleEntity role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new RoleNotFoundException(dto.getRoleId()));

        if (!allowedJobs.contains(actor.getJob())) {
            throw new NotAnActorException(actor.getFirstName() + " " + actor.getLastName() + " with job " + actor.getJob());
        }

        CastingEntity entity = new CastingEntity();
        entity.setActor(actor);
        entity.setMovie(movie);
        entity.setRole(role);
        entity.setId(new CastingId(movie.getId(), role.getId(), actor.getId()));

        return castingMapper.toDTO(castingRepository.save(entity));
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
        List<CastingEntity> entities = castingRepository.findByActorId(actorId);
        return entities.stream().map(castingMapper::toDTO).toList();
    }

    public CastingDTO update(Long movieId, Long roleId, Long actorId, CastingDTO dto) {
        CastingId castingId = new CastingId(movieId, roleId, actorId);
        CastingEntity existingEntity = castingRepository.findById(castingId)
                .orElseThrow(() -> new CastingNotFoundException(movieId, roleId, actorId));

        MovieEntity movie = existingEntity.getMovie();
        RoleEntity role = existingEntity.getRole();
        PersonEntity actor = existingEntity.getActor();

        if(dto.getMovieId() != null){
            movie = movieRepository.findById(dto.getMovieId())
                    .orElseThrow(() -> new MovieNotFoundException(dto.getMovieId()));

        }
        if(dto.getRoleId() != null){
            role = roleRepository.findById(dto.getRoleId())
                    .orElseThrow(() -> new RoleNotFoundException(dto.getRoleId()));
        }
        if(dto.getActorId() != null){
            actor = personRepository.findById(dto.getActorId())
                    .orElseThrow(() -> new PersonNotFoundException(dto.getActorId()));
            if(!allowedJobs.contains(actor.getJob())){
                throw new NotAnActorException(actor.getFirstName() + " " + actor.getLastName() + " with job " + actor.getJob());
            }

        }
        CastingEntity updatedCasting = new CastingEntity();
        updatedCasting.setId(new CastingId(movie.getId(), role.getId(), actor.getId()));
        updatedCasting.setMovie(movie);
        updatedCasting.setActor(actor);
        updatedCasting.setRole(role);

        castingRepository.delete(existingEntity);
        CastingEntity savedEntity = castingRepository.save(updatedCasting);
        return castingMapper.toDTO(savedEntity);
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

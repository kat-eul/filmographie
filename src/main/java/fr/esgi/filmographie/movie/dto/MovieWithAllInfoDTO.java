package fr.esgi.filmographie.movie.dto;

import fr.esgi.filmographie.genre.dto.GenreDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class MovieWithAllInfoDTO extends MovieDTO {
    private List<GenreDTO> genres;
}

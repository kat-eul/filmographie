package fr.esgi.filmographie.movie.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MovieDTO {

    private Long id;

    @NotBlank
    @Size(max = 200)
    private String title;

    private LocalDate releaseDate;

    @Size(max = 500)
    private String summary;
}

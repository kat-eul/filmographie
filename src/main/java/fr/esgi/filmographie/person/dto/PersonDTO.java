package fr.esgi.filmographie.person.dto;

import fr.esgi.filmographie.enums.JobEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PersonDTO {
    private Long id;

    @Size(max = 50)
    private String lastName;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String nickName;

    @NotEmpty
    @NotNull
    @Size(max = 50)
    private JobEnum job;
}

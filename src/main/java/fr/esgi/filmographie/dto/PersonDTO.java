package fr.esgi.filmographie.dto;

import fr.esgi.filmographie.enums.PersonEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
    private PersonEnum job;
}

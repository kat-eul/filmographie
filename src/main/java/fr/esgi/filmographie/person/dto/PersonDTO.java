package fr.esgi.filmographie.person.dto;

import fr.esgi.filmographie.enums.JobEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PersonDTO {
    private Long id;

    @Size(max = 50)
    private String lastName;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String nickName;

    // NB: @NotEmpty ne s'applique pas aux enums; @NotNull suffit pour la validation.
    @NotNull
    private JobEnum job;
}

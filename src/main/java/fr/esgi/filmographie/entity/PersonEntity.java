package fr.esgi.filmographie.entity;

import fr.esgi.filmographie.enums.PersonEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@Setter
public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(max = 50)
    private String lastName;

    @Length(max = 50)
    private String firstName;

    @Length(max = 50)
    private String nickName;

    @NotEmpty
    @NotNull
    @Length(max = 50)
    private PersonEnum job;
}

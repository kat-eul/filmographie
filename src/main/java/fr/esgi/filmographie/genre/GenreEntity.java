package fr.esgi.filmographie.genre;

import fr.esgi.filmographie.movie.MovieEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "genres")
public class GenreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    private final List<MovieEntity> movies = new ArrayList<>();

    public void addMovie(MovieEntity movie) {
        if (this.movies.stream().anyMatch(m -> m.getId().equals(movie.getId()))) {
            return;
        }

        this.movies.add(movie);
        movie.addGenre(this);
    }
}

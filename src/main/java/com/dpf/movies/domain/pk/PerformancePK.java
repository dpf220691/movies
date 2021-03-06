package com.dpf.movies.domain.pk;

import com.dpf.movies.domain.Actor;
import com.dpf.movies.domain.Movie;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PerformancePK implements Serializable {

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "actor_id")
    private Actor actor;

    @Override
    public String toString() {
        return "PerformancePK{" +
            "movie=" + movie.getId() +
            ", actor=" + actor.getId() +
            '}';
    }
}

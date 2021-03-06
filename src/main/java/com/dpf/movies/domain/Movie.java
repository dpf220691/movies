package com.dpf.movies.domain;

import com.dpf.movies.common.base.BaseEntity;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MOVIES")
public class Movie extends BaseEntity {

    public static final int YEAR_MIN = 1800;
    public static final int YEAR_MAX = 2200;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String title;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @Min(YEAR_MIN)
    @Max(YEAR_MAX)
    private int year;

    @OneToMany(mappedBy = "id.movie", cascade = CascadeType.ALL)
    private List<Performance> performances;

}

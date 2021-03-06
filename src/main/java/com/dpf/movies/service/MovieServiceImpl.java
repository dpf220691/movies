package com.dpf.movies.service;

import com.dpf.movies.common.base.BaseService;
import com.dpf.movies.common.exception.NotFoundException;
import com.dpf.movies.domain.Actor;
import com.dpf.movies.domain.Genre;
import com.dpf.movies.domain.Movie;
import com.dpf.movies.domain.Performance;
import com.dpf.movies.domain.pk.PerformancePK;
import com.dpf.movies.dto.MovieDetailOutDTO;
import com.dpf.movies.dto.MovieInDTO;
import com.dpf.movies.dto.MovieOutDTO;
import com.dpf.movies.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieServiceImpl extends BaseService implements MovieService {

    private MovieRepository movieRepository;
    private GenreService genreService;
    private ActorService actorService;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, GenreService genreService,
        ActorService actorService) {
        super();
        this.movieRepository = movieRepository;
        this.genreService = genreService;
        this.actorService = actorService;
    }

    @Override
    public List<MovieOutDTO> getAll(Pageable pageable) {
        Page<Movie> moviesRetrieved = movieRepository.findAll(pageable);
        return moviesRetrieved.getContent().stream()
            .map(movie -> getMapper().map(movie, MovieOutDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public MovieDetailOutDTO getById(Long id) {
        Movie movieRetrieved = getMovieById(id);
        return getMapper().map(movieRetrieved, MovieDetailOutDTO.class);
    }

    @Override
    public MovieDetailOutDTO save(MovieInDTO movieInDTO) {
        Movie movieToSave = generateMovie(movieInDTO);
        Movie movieSaved = movieRepository.save(movieToSave);
        return getMapper().map(movieSaved, MovieDetailOutDTO.class);
    }

    @Transactional
    @Override
    public MovieDetailOutDTO update(Long id, MovieInDTO movieInDTO) {
        getMovieById(id);

        Movie movieToUpdate = generateMovie(movieInDTO);
        movieToUpdate.setId(movieToUpdate.getId());

        Movie updatedMovie = movieRepository.save(movieToUpdate);
        return getMapper().map(updatedMovie, MovieDetailOutDTO.class);
    }

    @Override
    public MovieDetailOutDTO partialUpdate(Long id, MovieInDTO partialMovie) {
        ObjectMapper objectMapper = new ObjectMapper();

        MovieInDTO currentMovie = getMapper().map(getMovieById(id), MovieInDTO.class);
        Map<String, Object> currentMovieMap = objectMapper.convertValue(currentMovie, Map.class);

        Map<String, Object> partialMovieMap = objectMapper.convertValue(partialMovie, Map.class);
        partialMovieMap.values().removeAll(Collections.singleton(null));

        currentMovieMap.putAll(partialMovieMap);

        MovieInDTO newMovie = objectMapper.convertValue(currentMovieMap, MovieInDTO.class);
        return update(id, newMovie);
    }

    @Override
    public void delete(Long id) {
        getMovieById(id);
        movieRepository.deleteById(id);
    }

    private Movie getMovieById(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Movie not found with id=" + id.toString()));
        movie.getPerformances().size();
        return movie;
    }

    private Movie generateMovie(MovieInDTO movieInDTO) {
        Movie movie = getMapper().map(movieInDTO, Movie.class);
        movie.setGenre(getMapper().map(genreService.getById(movieInDTO.getGenreId()), Genre.class));
        Optional.ofNullable(movieInDTO.getActors())
            .ifPresent(actors -> movie.setPerformances(actors.stream()
                .map(id -> getMapper().map(actorService.getById(id), Actor.class))
                .map(actor -> Performance.builder()
                    .id(PerformancePK.builder()
                        .actor(actor)
                        .movie(movie)
                        .build())
                    .build())
                .collect(Collectors.toList())));
        return movie;
    }

}

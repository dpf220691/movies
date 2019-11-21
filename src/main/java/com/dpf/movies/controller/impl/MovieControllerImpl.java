package com.dpf.movies.controller.impl;

import com.dpf.movies.controller.MovieController;
import com.dpf.movies.core.base.BaseController;
import com.dpf.movies.dto.MovieDetailOutDTO;
import com.dpf.movies.dto.MovieInDTO;
import com.dpf.movies.dto.MovieOutDTO;
import com.dpf.movies.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${v1API}/movies")
public class MovieControllerImpl extends BaseController implements MovieController {

    private MovieService movieService;

    @Autowired
    public MovieControllerImpl(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public ResponseEntity<MovieDetailOutDTO> create(@RequestBody MovieInDTO movie) {
        MovieDetailOutDTO newMovie = movieService.save(movie);
        return new ResponseEntity<>(newMovie, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MovieOutDTO>> readAll(Pageable pageable) {
        List<MovieOutDTO> movies = movieService.getAll(pageable);
        return new ResponseEntity<>(movies, movies.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDetailOutDTO> read(@PathVariable Long id) {
        MovieDetailOutDTO movie = movieService.getById(id);
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieDetailOutDTO> update(@PathVariable Long id, @RequestBody MovieInDTO movie) {
        MovieDetailOutDTO updatedMovie = movieService.update(id, movie);
        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MovieDetailOutDTO> partialUpdate(@PathVariable Long id, @RequestBody MovieInDTO movie) {
        MovieDetailOutDTO updatedMovie = movieService.partialUpdate(id, movie);
        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        movieService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}

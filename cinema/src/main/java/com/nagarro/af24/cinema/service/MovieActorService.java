package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.dto.MovieDetailsDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MovieActorService {
    MovieDetailsDTO addActorToMovie(String movieTitle, List<String> actorsNames);

    MovieDetailsDTO getMovieDetails(String movieTitle);
}
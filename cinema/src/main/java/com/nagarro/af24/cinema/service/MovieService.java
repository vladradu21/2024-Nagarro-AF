package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.dto.ActorDTO;
import com.nagarro.af24.cinema.dto.MovieDTO;
import com.nagarro.af24.cinema.dto.MovieDetailsDTO;
import com.nagarro.af24.cinema.exception.CustomConflictException;
import com.nagarro.af24.cinema.exception.CustomNotFoundException;
import com.nagarro.af24.cinema.exception.ExceptionMessage;
import com.nagarro.af24.cinema.mapper.ActorMapper;
import com.nagarro.af24.cinema.mapper.MovieMapper;
import com.nagarro.af24.cinema.model.Actor;
import com.nagarro.af24.cinema.model.Movie;
import com.nagarro.af24.cinema.repository.ActorRepository;
import com.nagarro.af24.cinema.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final ActorRepository actorRepository;
    private final ActorMapper actorMapper;
    private final ImageStorageService imageStorageService;

    @Autowired
    public MovieService(MovieRepository movieRepository, MovieMapper movieMapper, ActorRepository actorRepository, ActorMapper actorMapper, ImageStorageService imageStorageService) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
        this.actorRepository = actorRepository;
        this.actorMapper = actorMapper;
        this.imageStorageService = imageStorageService;
    }

    public MovieDTO addMovie(MovieDTO movieDTO) {
        if (movieRepository.findByTitleAndYear(movieDTO.title(), movieDTO.year()).isPresent()) {
            throw new CustomConflictException(ExceptionMessage.MOVIE_ALREADY_EXISTS.formatMessage());
        }

        Movie movieToSave = movieMapper.toEntity(movieDTO);
        Movie savedMovie = movieRepository.save(movieToSave);
        return movieMapper.toDTO(savedMovie);
    }

    public MovieDTO getMovie(String title, int year) {
        Movie movie = movieRepository.findByTitleAndYear(title, year)
                .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.MOVIE_NOT_FOUND.formatMessage()));
        return movieMapper.toDTO(movie);
    }

    public MovieDetailsDTO getMovieDetails(String movieTitle, int year) {
        Movie movie = movieRepository.findByTitleAndYear(movieTitle, year)
                .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.MOVIE_NOT_FOUND.formatMessage()));

        MovieDTO movieDTO = movieMapper.toDTO(movie);
        List<Actor> actors = actorRepository.findByMovieTitleAndYear(movieTitle, year);
        List<ActorDTO> actorDTOS = actorMapper.toDTOs(actors);
        return new MovieDetailsDTO(movieDTO, actorDTOS);
    }

    public List<String> getMovieImagesUrls(String movieTitle, int year) {
        Movie movie = movieRepository.findByTitleAndYear(movieTitle, year)
                .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.MOVIE_NOT_FOUND.formatMessage()));
        return movie.getImagesPaths();
    }

    public MovieDTO updateMovie(MovieDTO movieDTO) {
        Movie movie = movieRepository.findByTitleAndYear(movieDTO.title(), movieDTO.year())
                .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.MOVIE_NOT_FOUND.formatMessage()));

        updateMovieFromDTO(movie, movieDTO);

        return movieMapper.toDTO(movieRepository.save(movie));
    }

    private void updateMovieFromDTO(Movie movie, MovieDTO movieDTO) {
        movie.setGenres(movieMapper.toEntity(movieDTO).getGenres());
        movie.setScore(movieDTO.score());
    }

    public List<String> uploadMovieImages(String title, int year, List<MultipartFile> files) {
        List<String> imagesPaths = imageStorageService.storeImages(files, "movie");

        updateMovieImagesPaths(title, year, imagesPaths);

        return movieRepository.findByTitleAndYear(title, year)
                .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.MOVIE_NOT_FOUND.formatMessage()))
                .getImagesPaths();
    }

    private void updateMovieImagesPaths(String title, int year, List<String> imagesPaths) {
        Movie movie = movieRepository.findByTitleAndYear(title, year)
                .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.MOVIE_NOT_FOUND.formatMessage()));
        movie.setImagesPaths(imagesPaths);
        movieRepository.save(movie);
    }

    public void deleteMovie(String title, int year) {
        Movie movie = movieRepository.findByTitleAndYear(title, year)
                .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.MOVIE_NOT_FOUND.formatMessage()));
        movieRepository.delete(movie);
    }
}
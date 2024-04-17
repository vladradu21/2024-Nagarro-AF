package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.dto.ActorDTO;
import com.nagarro.af24.cinema.dto.MovieDTO;
import com.nagarro.af24.cinema.dto.MovieDetailsDTO;
import com.nagarro.af24.cinema.dto.ReviewDTO;
import com.nagarro.af24.cinema.exception.CustomConflictException;
import com.nagarro.af24.cinema.exception.CustomNotFoundException;
import com.nagarro.af24.cinema.exception.ExceptionMessage;
import com.nagarro.af24.cinema.mapper.ActorMapper;
import com.nagarro.af24.cinema.mapper.MovieMapper;
import com.nagarro.af24.cinema.mapper.ReviewMapper;
import com.nagarro.af24.cinema.model.Actor;
import com.nagarro.af24.cinema.model.Movie;
import com.nagarro.af24.cinema.model.Review;
import com.nagarro.af24.cinema.repository.ActorRepository;
import com.nagarro.af24.cinema.repository.MovieRepository;
import com.nagarro.af24.cinema.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@EnableScheduling
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final ActorRepository actorRepository;
    private final ActorMapper actorMapper;
    private final ImageStorageService imageStorageService;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Autowired
    public MovieService(MovieRepository movieRepository, MovieMapper movieMapper, ActorRepository actorRepository, ActorMapper actorMapper, ImageStorageService imageStorageService, ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
        this.actorRepository = actorRepository;
        this.actorMapper = actorMapper;
        this.imageStorageService = imageStorageService;
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
    }

    public MovieDTO addMovie(MovieDTO movieDTO) {
        if (movieRepository.findByTitleAndYear(movieDTO.title(), movieDTO.year()).isPresent()) {
            throw new CustomConflictException(ExceptionMessage.MOVIE_ALREADY_EXISTS.formatMessage());
        }

        Movie movieToSave = movieMapper.toEntity(movieDTO);
        Movie savedMovie = movieRepository.save(movieToSave);
        return movieMapper.toDTO(savedMovie);
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

    public MovieDTO getMovie(String title, int year) {
        Movie movie = movieRepository.findByTitleAndYear(title, year)
                .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.MOVIE_NOT_FOUND.formatMessage()));
        return movieMapper.toDTO(movie);
    }

    public List<MovieDTO> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        if (movies.isEmpty()) {
            throw new CustomNotFoundException("No movies found");
        }
        return movieMapper.toDTOs(movies);
    }

    public MovieDetailsDTO getMovieDetails(String movieTitle, int year) {
        Movie movie = movieRepository.findByTitleAndYear(movieTitle, year)
                .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.MOVIE_NOT_FOUND.formatMessage()));

        MovieDTO movieDTO = movieMapper.toDTO(movie);
        List<Actor> actors = actorRepository.findByMovieTitleAndYear(movieTitle, year);
        List<ActorDTO> actorDTOS = actorMapper.toDTOs(actors);
        List<Review> reviews = reviewRepository.findByMovieTitleAndMovieYear(movieTitle, year);
        List<ReviewDTO> reviewDTOS = reviewMapper.toDTOs(reviews);
        return new MovieDetailsDTO(movieDTO, actorDTOS, reviewDTOS);
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
    }

    public void deleteMovie(String title, int year) {
        Movie movie = movieRepository.findByTitleAndYear(title, year)
                .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.MOVIE_NOT_FOUND.formatMessage()));
        movieRepository.delete(movie);
    }

    @Scheduled(fixedDelay = 60 * 1000, initialDelay = 5000)
    public void scheduledUpdateMoviesScores() {
        List<Movie> movies = movieRepository.findAllWithReviews();
        movies.forEach(movie -> {
            List<Review> reviews = movie.getReviews();
            double score = reviews.stream()
                    .mapToDouble(Review::getRating)
                    .average()
                    .orElse(0.0);
            movie.setScore(score);
            movieRepository.save(movie);
        });
    }
}
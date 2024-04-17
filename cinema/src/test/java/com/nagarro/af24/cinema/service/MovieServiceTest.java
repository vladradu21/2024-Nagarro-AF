package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.dto.ActorDTO;
import com.nagarro.af24.cinema.dto.MovieDTO;
import com.nagarro.af24.cinema.dto.MovieDetailsDTO;
import com.nagarro.af24.cinema.dto.ReviewDTO;
import com.nagarro.af24.cinema.mapper.ActorMapper;
import com.nagarro.af24.cinema.mapper.MovieMapper;
import com.nagarro.af24.cinema.mapper.ReviewMapper;
import com.nagarro.af24.cinema.model.Actor;
import com.nagarro.af24.cinema.model.Movie;
import com.nagarro.af24.cinema.model.Review;
import com.nagarro.af24.cinema.repository.ActorRepository;
import com.nagarro.af24.cinema.repository.MovieRepository;
import com.nagarro.af24.cinema.repository.ReviewRepository;
import com.nagarro.af24.cinema.utils.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private MovieMapper movieMapper;
    @Mock
    private ActorRepository actorRepository;
    @Mock
    private ActorMapper actorMapper;
    @Mock
    private ImageStorageService imageStorageService;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ReviewMapper reviewMapper;
    @InjectMocks
    private MovieService movieService;

    @Test
    void addMovie() {
        Movie movie = TestData.getMovie();
        MovieDTO movieDTO = TestData.getMovieDTO();
        Movie savedMovie = TestData.getMovie();
        MovieDTO savedMovieDTO = TestData.getMovieDTO();
        when(movieRepository.findByTitleAndYear(movieDTO.title(), movieDTO.year())).thenReturn(Optional.empty());
        when(movieMapper.toEntity(movieDTO)).thenReturn(movie);
        when(movieRepository.save(movie)).thenReturn(savedMovie);
        when(movieMapper.toDTO(savedMovie)).thenReturn(savedMovieDTO);

        MovieDTO result = movieService.addMovie(movieDTO);

        Assertions.assertEquals(savedMovieDTO, result);
    }

    @Test
    void testUploadMovieImages() {
        Movie movie = TestData.getMovie();
        String title = movie.getTitle();
        int year = movie.getYear();
        List<MultipartFile> files = TestData.getMockMultipartFiles();
        List<String> imagesPaths = List.of("Path1", "Path2");
        when(imageStorageService.storeImages(files, "movie")).thenReturn(imagesPaths);
        when(movieRepository.findByTitleAndYear(title, year)).thenReturn(Optional.of(movie));

        List<String> resultImagesPaths = movieService.uploadMovieImages(title, year, files);

        Assertions.assertEquals(imagesPaths, resultImagesPaths);
    }

    @Test
    void getMovie() {
        Movie movie = TestData.getMovie();
        MovieDTO movieDTO = TestData.getMovieDTO();
        when(movieRepository.findByTitleAndYear(movieDTO.title(), movieDTO.year())).thenReturn(Optional.of(movie));
        when(movieMapper.toDTO(movie)).thenReturn(movieDTO);

        MovieDTO result = movieService.getMovie(movieDTO.title(), movieDTO.year());

        Assertions.assertEquals(movieDTO, result);
    }

    @Test
    void getAllMovies() {
        List<Movie> movies = TestData.getMovies();
        List<MovieDTO> movieDTOs = TestData.getMoviesDTO();
        when(movieRepository.findAll()).thenReturn(movies);
        when(movieMapper.toDTOs(movies)).thenReturn(movieDTOs);

        List<MovieDTO> result = movieService.getAllMovies();

        Assertions.assertEquals(movieDTOs, result);
    }

    @Test
    void getMovieDetails() {
        Movie movie = TestData.getMovie();
        String title = movie.getTitle();
        int year = movie.getYear();
        when(movieRepository.findByTitleAndYear(title, year)).thenReturn(Optional.of(movie));
        MovieDTO movieDTO = TestData.getMovieDTO();
        when(movieMapper.toDTO(movie)).thenReturn(movieDTO);
        List<Actor> actors = TestData.getActors();
        when(actorRepository.findByMovieTitleAndYear(title, year)).thenReturn(actors);
        List<ActorDTO> actorDTOS = TestData.getActorDTOs();
        when(actorMapper.toDTOs(actors)).thenReturn(actorDTOS);
        List<Review> reviews = TestData.getReviews();
        when(reviewRepository.findByMovieTitleAndMovieYear(title, year)).thenReturn(reviews);
        List<ReviewDTO> reviewDTOS = TestData.getReviewDTOs();
        when(reviewMapper.toDTOs(reviews)).thenReturn(reviewDTOS);
        MovieDetailsDTO expected = new MovieDetailsDTO(movieDTO, actorDTOS, reviewDTOS);

        MovieDetailsDTO result = movieService.getMovieDetails(title, year);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void testGetMovieImagesUrls() {
        Movie movie = TestData.getMovie();
        String title = movie.getTitle();
        int year = movie.getYear();
        when(movieRepository.findByTitleAndYear(title, year)).thenReturn(Optional.of(movie));

        List<String> result = movieService.getMovieImagesUrls(title, year);

        Assertions.assertEquals(movie.getImagesPaths(), result);
    }

    @Test
    void updateMovie() {
        Movie movie = TestData.getMovie();
        MovieDTO movieDTO = TestData.getMovieDTO();
        Movie updatedMovie = TestData.getMovie();
        MovieDTO updatedMovieDTO = TestData.getMovieDTO();
        when(movieRepository.findByTitleAndYear(movieDTO.title(), movieDTO.year())).thenReturn(Optional.of(movie));
        when(movieMapper.toEntity(movieDTO)).thenReturn(movie);
        when(movieRepository.save(movie)).thenReturn(updatedMovie);
        when(movieMapper.toDTO(updatedMovie)).thenReturn(updatedMovieDTO);

        MovieDTO result = movieService.updateMovie(movieDTO);

        Assertions.assertEquals(updatedMovieDTO, result);
    }

    @Test
    void deleteMovie() {
        Movie movie = TestData.getMovie();
        String title = movie.getTitle();
        int year = movie.getYear();
        when(movieRepository.findByTitleAndYear(title, year)).thenReturn(Optional.of(movie));

        movieService.deleteMovie(title, year);

        Mockito.verify(movieRepository).delete(movie);
    }
}
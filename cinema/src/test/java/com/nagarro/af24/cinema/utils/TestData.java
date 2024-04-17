package com.nagarro.af24.cinema.utils;

import com.nagarro.af24.cinema.dto.ActorDTO;
import com.nagarro.af24.cinema.dto.LoginDTO;
import com.nagarro.af24.cinema.dto.MovieActorsDTO;
import com.nagarro.af24.cinema.dto.MovieDTO;
import com.nagarro.af24.cinema.dto.MovieDetailsDTO;
import com.nagarro.af24.cinema.dto.RegisterDTO;
import com.nagarro.af24.cinema.dto.ReviewDTO;
import com.nagarro.af24.cinema.dto.UserDTO;
import com.nagarro.af24.cinema.dto.UserUpdateDTO;
import com.nagarro.af24.cinema.model.Actor;
import com.nagarro.af24.cinema.model.ApplicationUser;
import com.nagarro.af24.cinema.model.Gender;
import com.nagarro.af24.cinema.model.Movie;
import com.nagarro.af24.cinema.model.Review;
import com.nagarro.af24.cinema.model.Role;
import lombok.SneakyThrows;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.lenient;

public class TestData {
    public static Movie getMovie() {
        return new Movie(null, "The Shawshank Redemption", null, 1994, 9.3, null, null, List.of("Path1", "Path2"));
    }

    public static List<Movie> getMovies() {
        return List.of(
                new Movie(null, "The Shawshank Redemption", null, 1994, 9.3, null, null, List.of("Path1", "Path2")),
                new Movie(null, "The Godfather", null, 1972, 9.2, null, null, List.of("Path3", "Path4")));
    }

    public static Actor getActor() {
        return new Actor(null, "Tim Robbins", 30, Gender.MALE, null, null);
    }

    public static List<Actor> getActors() {
        return List.of(
                new Actor(null, "Tim Robbins", 30, Gender.MALE, null, null),
                new Actor(null, "Morgan Freeman", 70, Gender.MALE, null, null));
    }

    public static MovieDTO getMovieDTO() {
        return new MovieDTO("Shawshank Redemption", Set.of("Drama", "Crime"), 1994, 9.3);
    }

    public static List<MovieDTO> getMoviesDTO() {
        return List.of(
                new MovieDTO("Shawshank Redemption", Set.of("Drama", "Crime"), 1994, 9.3),
                new MovieDTO("The Godfather", Set.of("Drama", "Crime"), 1972, 9.2));
    }

    public static MovieDTO getUpdatedMovieDTO() {
        return new MovieDTO("Shawshank Redemption", Set.of("Drama", "Crime", "Action"), 1994, 9.5);
    }

    public static MovieDTO getMovieDTOWithWrongGenre() {
        return new MovieDTO("Shawshank Redemption", Set.of("Drama", "Wrong Genre"), 1994, 9.3);
    }

    public static ActorDTO getActorDTO() {
        return new ActorDTO("Tim Robbins", 30, "MALE", "United States");
    }

    public static ActorDTO getUpdatedActorDTO() {
        return new ActorDTO("Tim Robbins", 30, "MALE", "Australia");
    }

    public static ActorDTO getActorDTOWithWrongCountry() {
        return new ActorDTO("Tim Robbins", 30, "MALE", "Wrong Country");
    }

    public static List<ActorDTO> getActorDTOs() {
        return List.of(
                new ActorDTO("Tim Robbins", 30, "MALE", "United States"),
                new ActorDTO("Morgan Freeman", 70, "MALE", "United States"));
    }

    public static List<String> getActorsNames() {
        return List.of("Tim Robbins", "Morgan Freeman");
    }

    public static Review getReview() {
        return new Review(null, "The Shawshank Redemption, review", 9.3, "Liked it!", null, null);
    }

    public static List<Review> getReviews() {
        return List.of(
                new Review(null, "Let's talk about The Shawshank Redemption", 9.3, "Great movie!", null, null),
                new Review(null, "The Shawshank Redemption, review", 9.3, "Liked it!", null, null));
    }

    public static ReviewDTO getReviewDTO() {
        return new ReviewDTO("The Shawshank Redemption, review", 9.3, "Liked it!", "Shawshank Redemption", 1994, "johndoe");
    }

    public static List<ReviewDTO> getReviewDTOs() {
        return List.of(
                new ReviewDTO("Let's talk about The Shawshank Redemption", 9.3, "Great movie!", "Shawshank Redemption", 1994, null),
                new ReviewDTO("The Shawshank Redemption, review", 9.3, "Liked it!", "Shawshank Redemption", 1994, null));
    }

    public static MovieDetailsDTO getMovieDetailsDTO() {
        return new MovieDetailsDTO(getMovieDTO(), getActorDTOs(), null);
    }

    @SneakyThrows
    public static List<MultipartFile> getMockMultipartFiles() {
        MultipartFile mockFile1 = Mockito.mock(MultipartFile.class);
        lenient().when(mockFile1.getOriginalFilename()).thenReturn("file1.jpg");
        lenient().when(mockFile1.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[1024]));

        MultipartFile mockFile2 = Mockito.mock(MultipartFile.class);
        lenient().when(mockFile2.getOriginalFilename()).thenReturn("file2.png");
        lenient().when(mockFile2.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[2048]));

        return List.of(mockFile1, mockFile2);
    }

    public static List<MockMultipartFile> getMockedMultipartFiles() {
        MockMultipartFile mockFile1 = new MockMultipartFile(
                "images", "file1.jpg", "multipart/form-data", new byte[1024]);

        MockMultipartFile mockFile2 = new MockMultipartFile(
                "images", "file2.png", "multipart/form-data", new byte[2048]);

        return List.of(mockFile1, mockFile2);
    }

    public static ApplicationUser getApplicationUser() {
        return new ApplicationUser("John", "Doe", "johndoe@gmail.com", "johndoe", "password", null, null);
    }

    public static Set<Role> getRoles() {
        return Set.of(new Role("USER"), new Role("ADMIN"));
    }

    public static RegisterDTO getRegisterDTO() {
        return new RegisterDTO("John", "Doe", "johndoe@gmail.com", "johndoe", "password");
    }

    public static UserDTO getUserDTO() {
        return new UserDTO("John", "Doe", "johndoe@gmail.com", "johndoe");
    }

    public static LoginDTO getLoginDTO() {
        return new LoginDTO("johndoe", "password");
    }

    public static Authentication getAuthentication() {
        return Mockito.mock(Authentication.class);
    }

    public static UserUpdateDTO getUserUpdateDTO() {
        return new UserUpdateDTO("John", "Doe", "johndoe@gmail.com", "johndoe", "password", List.of("USER"));
    }

    public static UserUpdateDTO getUserUpdateDTODifferentData() {
        return new UserUpdateDTO("Different", "Data", "DifferentData@gmail.com", "johndoe", "password", List.of("USER"));
    }

    public static MovieActorsDTO getMovieActorsDTO() {
        return new MovieActorsDTO(getMovieDTO(), getActorDTOs());
    }
}
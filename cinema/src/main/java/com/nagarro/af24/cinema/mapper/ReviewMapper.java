package com.nagarro.af24.cinema.mapper;

import com.nagarro.af24.cinema.dto.ReviewDTO;
import com.nagarro.af24.cinema.exception.CustomNotFoundException;
import com.nagarro.af24.cinema.exception.ExceptionMessage;
import com.nagarro.af24.cinema.model.Movie;
import com.nagarro.af24.cinema.model.Review;
import com.nagarro.af24.cinema.repository.MovieRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ReviewMapper {
    @Autowired
    private MovieRepository movieRepository;

    @Mapping(target = "movieTitle", source = "movie.title")
    @Mapping(target = "movieProductionYear", source = "movie.year")
    public abstract ReviewDTO toDTO(Review review);

    @Mapping(target = "movie", source = "reviewDTO", qualifiedByName = "titleAndYearToMovie")
    @Mapping(target = "id", ignore = true)
    public abstract Review toEntity(ReviewDTO reviewDTO);

    public abstract List<ReviewDTO> toDTOs(List<Review> reviews);

    public abstract List<Review> toEntities(List<ReviewDTO> reviewDTOs);

    @Named("titleAndYearToMovie")
    Movie titleAndYearToMovieEntity(ReviewDTO reviewDTO) {
        return movieRepository.findByTitleAndYear(reviewDTO.movieTitle(), reviewDTO.movieProductionYear())
                .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.MOVIE_NOT_FOUND.formatMessage()));
    }
}
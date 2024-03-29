package com.nagarro.af24.cinema.mapper;

import com.nagarro.af24.cinema.dto.MovieDTO;
import com.nagarro.af24.cinema.exception.CustomNotFoundException;
import com.nagarro.af24.cinema.exception.ExceptionMessage;
import com.nagarro.af24.cinema.model.Genre;
import com.nagarro.af24.cinema.model.Movie;
import com.nagarro.af24.cinema.repository.GenreRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class MovieMapper {
    @Autowired
    private GenreRepository genreRepository;

    @Mapping(target = "genres", source = "genres", qualifiedByName = "genreSetToStringSet")
    public abstract MovieDTO toDTO(Movie movie);

    @Mapping(target = "genres", ignore = true)
    public abstract Movie toEntity(MovieDTO movieDTO);

    public abstract List<MovieDTO> toDTOs(List<Movie> movies);

    public abstract List<Movie> toEntities(List<MovieDTO> movieDTOs);

    @Named("genreSetToStringSet")
    Set<String> genreSetToStringSet(Set<Genre> genres) {
        return genres.stream().map(Genre::getType).collect(Collectors.toSet());
    }

    @AfterMapping
    protected void mapGenresToEntity(MovieDTO movieDTO, @MappingTarget Movie movie) {
        if (!movieDTO.genres().isEmpty()) {
            Set<Genre> genres = movieDTO.genres().stream()
                    .map(type -> genreRepository.findByType(type)
                            .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.GENRE_NOT_FOUND.formatMessage(type))))
                    .collect(Collectors.toSet());
            movie.setGenres(genres);
        }
    }
}
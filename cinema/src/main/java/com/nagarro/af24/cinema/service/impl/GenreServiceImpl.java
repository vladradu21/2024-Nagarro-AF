package com.nagarro.af24.cinema.service.impl;

import com.nagarro.af24.cinema.repository.GenreRepository;
import com.nagarro.af24.cinema.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }
}

package com.nagarro.af24.cinema.service.impl;

import com.nagarro.af24.cinema.repository.CountryRepository;
import com.nagarro.af24.cinema.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }
}

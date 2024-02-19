package com.nagarro.af24.cinema.mapper;

import com.nagarro.af24.cinema.dto.ActorDTO;
import com.nagarro.af24.cinema.exception.CustomNotFoundException;
import com.nagarro.af24.cinema.model.Actor;
import com.nagarro.af24.cinema.model.Country;
import com.nagarro.af24.cinema.model.Gender;
import com.nagarro.af24.cinema.repository.CountryRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ActorMapper {
    @Autowired
    private CountryRepository countryRepository;

    @Mapping(target = "gender", source = "gender", qualifiedByName = "genderToString")
    @Mapping(target = "country", source = "country.name")
    public abstract ActorDTO toDTO(Actor actor);

    @Mapping(target = "gender", source = "gender", qualifiedByName = "stringToGender")
    @Mapping(target = "country", ignore = true)
    public abstract Actor toEntity(ActorDTO actorDTO);

    public abstract List<ActorDTO> toDTOs(List<Actor> actors);

    public abstract List<Actor> toEntities(List<ActorDTO> actorDTOs);

    @Named("genderToString")
    String genderToString(Gender gender) {
        return gender.toString();
    }

    @Named("stringToGender")
    Gender stringToGender(String gender) {
        return Gender.valueOf(gender);
    }

    @AfterMapping
    protected void mapCountryToEntity(ActorDTO actorDTO, @MappingTarget Actor actor) {
        if (actorDTO.country() != null) {
            Country country = countryRepository.findByName(actorDTO.country())
                    .orElseThrow(() -> new CustomNotFoundException("Country not found for: " + actorDTO.country()));
            actor.setCountry(country);
        }
    }
}
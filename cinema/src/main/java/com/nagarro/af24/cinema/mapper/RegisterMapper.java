package com.nagarro.af24.cinema.mapper;

import com.nagarro.af24.cinema.dto.RegisterDTO;
import com.nagarro.af24.cinema.model.ApplicationUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class RegisterMapper {
    public abstract ApplicationUser toEntity(RegisterDTO registerDTO);
}
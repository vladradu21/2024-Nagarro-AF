package com.nagarro.af24.cinema.mapper;

import com.nagarro.af24.cinema.dto.UserDTO;
import com.nagarro.af24.cinema.model.ApplicationUser;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    public abstract UserDTO toDTO(ApplicationUser user);

    public abstract List<UserDTO> toDTOs(List<ApplicationUser> users);

    public abstract ApplicationUser toEntity(UserDTO userDTO);

    public abstract List<ApplicationUser> toEntities(List<UserDTO> userDTOs);
}
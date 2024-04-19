package com.nagarro.af24.cinema.mapper;

import com.nagarro.af24.cinema.dto.OtpDTO;
import com.nagarro.af24.cinema.exception.CustomNotFoundException;
import com.nagarro.af24.cinema.exception.ExceptionMessage;
import com.nagarro.af24.cinema.model.ApplicationUser;
import com.nagarro.af24.cinema.model.Otp;
import com.nagarro.af24.cinema.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class OtpMapper {
    @Autowired
    private UserRepository userRepository;

    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "code", target = "otp")
    @Mapping(source = "expireAt", target = "expireAt")
    public abstract OtpDTO toDTO(Otp otp);

    @Mapping(target = "user", source = "otpDTO", qualifiedByName = "emailToUser")
    @Mapping(target = "code", source = "otpDTO.otp")
    @Mapping(target = "expireAt", source = "otpDTO.expireAt")
    public abstract Otp toEntity(OtpDTO otpDTO);

    @Named("emailToUser")
    ApplicationUser emailToUserEntity(OtpDTO otpDTO) {
        return userRepository.findByEmail(otpDTO.email())
                .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.USER_NOT_FOUND.formatMessage()));
    }
}
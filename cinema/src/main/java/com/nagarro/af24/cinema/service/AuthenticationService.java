package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.dto.LoginDTO;
import com.nagarro.af24.cinema.dto.RegisterDTO;
import com.nagarro.af24.cinema.dto.ResponseDTO;
import com.nagarro.af24.cinema.dto.UserDTO;
import com.nagarro.af24.cinema.exception.CustomConflictException;
import com.nagarro.af24.cinema.exception.ExceptionMessage;
import com.nagarro.af24.cinema.mapper.RegisterMapper;
import com.nagarro.af24.cinema.mapper.UserMapper;
import com.nagarro.af24.cinema.model.ApplicationUser;
import com.nagarro.af24.cinema.model.Role;
import com.nagarro.af24.cinema.repository.RoleRepository;
import com.nagarro.af24.cinema.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RegisterMapper registerMapper;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Autowired
    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, RegisterMapper registerMapper, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.registerMapper = registerMapper;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public UserDTO register(RegisterDTO registerDTO) {
        checkUserDetails(registerDTO);

        ApplicationUser userToSave = registerMapper.toEntity(registerDTO);

        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));

        Role userRole = roleRepository.findByAuthority("USER")
                .orElseThrow(() -> new CustomConflictException("Role not found"));
        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);
        userToSave.setAuthorities(authorities);

        ApplicationUser savedUser = userRepository.save(userToSave);
        return userMapper.toDTO(savedUser);
    }

    private void checkUserDetails(RegisterDTO registerDTO) {
        if (userRepository.findByUsername(registerDTO.username()).isPresent()) {
            throw new CustomConflictException("Username already exists");
        }
        if (userRepository.findByEmail(registerDTO.email()).isPresent()) {
            throw new CustomConflictException("Email already exists");
        }
    }

    public ResponseDTO login(LoginDTO loginDTO) {

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password())
            );

            String token = tokenService.generateJwt(auth);
            ApplicationUser user = userRepository.findByUsername(loginDTO.username())
                    .orElseThrow(() -> new CustomConflictException(ExceptionMessage.USER_NOT_FOUND.formatMessage()));
            return new ResponseDTO(userMapper.toDTO(user), token);

        } catch (AuthenticationException e) {
            throw new CustomConflictException("Invalid username/password supplied");
        }
    }
}
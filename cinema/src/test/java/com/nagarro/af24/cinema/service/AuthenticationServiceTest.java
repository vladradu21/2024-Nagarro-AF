package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.dto.LoginDTO;
import com.nagarro.af24.cinema.dto.RegisterDTO;
import com.nagarro.af24.cinema.dto.ResponseDTO;
import com.nagarro.af24.cinema.dto.UserDTO;
import com.nagarro.af24.cinema.mapper.RegisterMapper;
import com.nagarro.af24.cinema.mapper.UserMapper;
import com.nagarro.af24.cinema.model.ApplicationUser;
import com.nagarro.af24.cinema.model.Role;
import com.nagarro.af24.cinema.repository.RoleRepository;
import com.nagarro.af24.cinema.repository.UserRepository;
import com.nagarro.af24.cinema.utils.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @Mock
    private RegisterMapper registerMapper;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenService tokenService;
    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void testRegister() {
        RegisterDTO registerDTO = TestData.getRegisterDTO();
        ApplicationUser userToSave = TestData.getApplicationUser();
        when(registerMapper.toEntity(registerDTO)).thenReturn(userToSave);
        when(passwordEncoder.encode(userToSave.getPassword())).thenReturn("encodedPassword");
        Role userRole = new Role("USER");
        when(roleRepository.findByAuthority("USER")).thenReturn(java.util.Optional.of(userRole));
        ApplicationUser savedUser = TestData.getApplicationUser();
        when(userRepository.save(userToSave)).thenReturn(savedUser);
        UserDTO userDTO = TestData.getUserDTO();
        when(userMapper.toDTO(savedUser)).thenReturn(userDTO);

        UserDTO result = authenticationService.register(registerDTO);

        assertEquals(userDTO, result);
    }

    @Test
    void testLogin() {
        LoginDTO loginDTO = TestData.getLoginDTO();
        Authentication authentication = TestData.getAuthentication();
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password()))).thenReturn(authentication);
        String token = "token";
        when(tokenService.generateJwt(authentication)).thenReturn(token);
        ApplicationUser user = TestData.getApplicationUser();
        when(userRepository.findByUsername(loginDTO.username())).thenReturn(java.util.Optional.of(user));
        UserDTO userDTO = TestData.getUserDTO();
        when(userMapper.toDTO(user)).thenReturn(userDTO);
        ResponseDTO expected = new ResponseDTO(userDTO, token);

        ResponseDTO result = authenticationService.login(loginDTO);

        assertEquals(expected, result);
    }
}
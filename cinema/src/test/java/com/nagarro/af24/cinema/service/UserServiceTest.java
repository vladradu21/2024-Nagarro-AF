package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.dto.UserDTO;
import com.nagarro.af24.cinema.dto.UserUpdateDTO;
import com.nagarro.af24.cinema.mapper.UserMapper;
import com.nagarro.af24.cinema.model.ApplicationUser;
import com.nagarro.af24.cinema.repository.RoleRepository;
import com.nagarro.af24.cinema.repository.UserRepository;
import com.nagarro.af24.cinema.utils.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private UserService userService;

    @Test
    void getUserByUsername() {
        ApplicationUser user = TestData.getApplicationUser();
        String username = user.getUsername();
        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(user));
        UserDTO userDTO = TestData.getUserDTO();
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserByUsername(username);

        assertEquals(userDTO, result);
    }

    @Test
    void updateUser() {
        ApplicationUser user = TestData.getApplicationUser();
        UserUpdateDTO userDTO = TestData.getUserUpdateDTO();
        when(userRepository.findByUsername(userDTO.username())).thenReturn(java.util.Optional.of(user));
        ApplicationUser savedUser = TestData.getApplicationUser();
        when(userRepository.save(user)).thenReturn(savedUser);
        UserDTO savedUserDTO = TestData.getUserDTO();
        when(userMapper.toDTO(savedUser)).thenReturn(savedUserDTO);

        UserDTO result = userService.updateUser(userDTO);

        assertEquals(savedUserDTO, result);
        verify(roleRepository).findAllByAuthorityIn(userDTO.roles());
        verify(passwordEncoder).encode(userDTO.password());
    }

    @Test
    void deleteUser() {
        ApplicationUser user = TestData.getApplicationUser();
        String username = user.getUsername();
        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(user));

        userService.deleteUser(username);

        verify(userRepository).delete(user);
    }
}
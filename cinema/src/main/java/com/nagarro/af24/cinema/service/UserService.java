package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.dto.UserDTO;
import com.nagarro.af24.cinema.dto.UserUpdateDTO;
import com.nagarro.af24.cinema.exception.CustomNotFoundException;
import com.nagarro.af24.cinema.exception.ExceptionMessage;
import com.nagarro.af24.cinema.mapper.UserMapper;
import com.nagarro.af24.cinema.model.ApplicationUser;
import com.nagarro.af24.cinema.model.Role;
import com.nagarro.af24.cinema.repository.RoleRepository;
import com.nagarro.af24.cinema.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.USER_NOT_FOUND.formatMessage()));
    }

    public UserDTO getUserByUsername(String username) {
        ApplicationUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.USER_NOT_FOUND.formatMessage()));
        return userMapper.toDTO(user);
    }

    public UserDTO updateUser(UserUpdateDTO userDTO) {
        ApplicationUser user = userRepository.findByUsername(userDTO.username())
                .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.USER_NOT_FOUND.formatMessage()));

        updateUserFromDTO(user, userDTO);
        return userMapper.toDTO(userRepository.save(user));
    }

    private void updateUserFromDTO(ApplicationUser user, UserUpdateDTO userDTO) {
        user.setFirstname(userDTO.firstname());
        user.setLastname(userDTO.lastname());
        user.setPassword(passwordEncoder.encode(userDTO.password()));
        user.setEmail(userDTO.email());
        Set<Role> authorities = new HashSet<>(roleRepository.findAllByAuthorityIn(userDTO.roles()));
        user.setAuthorities(authorities);
    }

    public void deleteUser(String username) {
        ApplicationUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNotFoundException(ExceptionMessage.USER_NOT_FOUND.formatMessage()));
        userRepository.delete(user);
    }
}
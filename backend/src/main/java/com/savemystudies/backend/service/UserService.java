package com.savemystudies.backend.service;

import com.savemystudies.backend.dto.UserLoginDto;
import com.savemystudies.backend.dto.UserRegistrationDto;
import com.savemystudies.backend.model.User;
import com.savemystudies.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User registerNewUser(UserRegistrationDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }
        String senhaHash = passwordEncoder.encode(userDto.getSenha());
        User user = User.builder()
                .nome(userDto.getNome())
                .email(userDto.getEmail())
                .senhaHash(senhaHash)
                .build();
        return userRepository.save(user);
    }

    public User authenticateUser(UserLoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("E-mail ou senha incorretos."));
        if (!passwordEncoder.matches(loginDto.getSenha(), user.getSenhaHash())) {
            throw new IllegalArgumentException("E-mail ou senha incorretos.");
        }
        return user;
    }

    public User salvarUsuario(User usuario) {
        return userRepository.save(usuario);
    }
}
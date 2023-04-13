package com.plannerapp.service;

import com.plannerapp.config.LoggedUser;
import com.plannerapp.model.dtos.LoginDto;
import com.plannerapp.model.dtos.RegisterDto;
import com.plannerapp.model.entity.User;
import com.plannerapp.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final LoggedUser userSession;

    public AuthService(UserRepository userRepository, LoggedUser userSession) {
        this.userRepository = userRepository;
        this.userSession = userSession;
    }

    public boolean isLoggedIn() {
        return this.userSession.getId() > 0;
    }

    public boolean register(RegisterDto registerDto) {
        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            return false;
        }

        Optional<User> optUserEmail = this.userRepository.findByEmail(registerDto.getEmail());
        if (optUserEmail.isPresent()) {
            return false;
        }
        Optional<User> optUserByUsername = this.userRepository.findByUsername(registerDto.getUsername());
        if (optUserByUsername.isPresent()) {
            return false;
        }

        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(registerDto.getPassword());

        userRepository.save(user);

        return true;
    }

    public boolean login(LoginDto loginDto) {
        Optional<User> optUser = this.userRepository
                .findByUsernameAndPassword(loginDto.getUsername(), loginDto.getPassword());
        if (optUser.isEmpty()) {
            return false;
        }
        this.userSession.login(optUser.get());
        return true;
    }

    public void logout() {
        this.userSession.logout();
    }

}

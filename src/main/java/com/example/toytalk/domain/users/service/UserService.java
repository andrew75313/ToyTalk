package com.example.toytalk.domain.users.service;

import com.example.toytalk.domain.users.dto.SignupRequestDTO;
import com.example.toytalk.domain.users.dto.UserResponseDTO;
import com.example.toytalk.domain.users.dto.UserUpdateRequestDTO;
import com.example.toytalk.domain.users.entity.User;
import com.example.toytalk.domain.users.entity.UserRole;
import com.example.toytalk.domain.users.entity.UserStatus;
import com.example.toytalk.domain.users.repository.UserRepository;
import com.example.toytalk.global.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(@Valid SignupRequestDTO requestDTO) {
        String username = requestDTO.getUsername();
        String password = passwordEncoder.encode(requestDTO.getPassword());
        String email = requestDTO.getEmail();
        String phoneNumber = requestDTO.getPhoneNumber();

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("아이디가 이미 존재합니다.");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이메일이 이미 존재합니다.");
        }

        User user = User.builder()
                .username(username)
                .password(password)
                .email(email)
                .phoneNumber(phoneNumber)
                .role(UserRole.USER)
                .status(UserStatus.ACTIVATED)
                .build();

        userRepository.save(user);

        return new UserResponseDTO(user);
    }

    public UserResponseDTO getUser(UUID userId) {
        User foundUser = userRepository.findActivatedById(userId).orElseThrow(
                () -> new UserNotFoundException("사용자를 찾을 수 없습니다.")
        );

        return new UserResponseDTO(foundUser);
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAllActivated();

        return users.stream().map(UserResponseDTO::new).toList();
    }

    @Transactional
    public UserResponseDTO updateUser(UUID userId, @Valid UserUpdateRequestDTO requestDTO) {
        User foundUser = userRepository.findActivatedById(userId).orElseThrow(
                () -> new UserNotFoundException("사용자를 찾을 수 없습니다.")
        );

        String newEmail = requestDTO.getNewEmail();
        String newPhoneNumber = requestDTO.getNewPhoneNumber();

        if (!newEmail.isEmpty()) {
            foundUser.updateEmail(newEmail);
        }

        if (!newPhoneNumber.isEmpty()) {
            foundUser.updatePhoneNumber(newPhoneNumber);
        }

        return new UserResponseDTO(foundUser);
    }

    @Transactional
    public void deleteUser(UUID userId) {
        User foundUser = userRepository.findActivatedById(userId).orElseThrow(
                () -> new UserNotFoundException("사용자를 찾을 수 없습니다.")
        );

        foundUser.updateStatus(UserStatus.DEACTIVATED);
    }
}

package com.example.toytalk.global.security.user;

import com.example.toytalk.domain.users.entity.User;
import com.example.toytalk.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findActivatedByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find user."));

        return new UserDetailsImpl(user);
    }

    public UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        User user = userRepository.findActivatedById(UUID.fromString(userId))
        .orElseThrow(() -> new UsernameNotFoundException("Can't find user."));

        return new UserDetailsImpl(user);
    }
}

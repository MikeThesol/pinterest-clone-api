package com.thesol.pinterest_clone.Services;

import com.thesol.pinterest_clone.dto.JwtAuthenticationDto;
import com.thesol.pinterest_clone.dto.RefreshTokenDto;
import com.thesol.pinterest_clone.dto.UserCredentialsDto;
import com.thesol.pinterest_clone.models.User;
import com.thesol.pinterest_clone.repositories.UserRepository;
import com.thesol.pinterest_clone.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public User getUserById(Long id) throws ChangeSetPersister.NotFoundException {
        return userRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    public User getUserByEmail(String email) throws ChangeSetPersister.NotFoundException {
        return userRepository.findByEmail(email).orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    public JwtAuthenticationDto signIn(UserCredentialsDto userCredentialsDto) {
        User user = findByCredentials(userCredentialsDto);
        return jwtService.generateJwtAuthToken(user.getEmail());
    }

    public JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws Exception {
        String refreshToken = refreshTokenDto.getRefreshToken();
        if(refreshToken == null && jwtService.validateJwtToken(refreshToken)) {
            User user = findByEmail(jwtService.getEmailFromToken(refreshToken));
            return jwtService.refreshBaseToken(user.getEmail(), refreshToken);
        }
        throw new AuthenticationServiceException("Invalid refresh token");
    }

    public String addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User created";
    }

    private User findByCredentials(UserCredentialsDto userCredentialsDto) {
        Optional<User> optionalUser = userRepository.findByEmail(userCredentialsDto.getEmail());
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(passwordEncoder.matches(userCredentialsDto.getPassword(), user.getPassword())) {
                return user;
            }
        }
        throw new AuthenticationServiceException("Email or password isn't correct");
    }

    private User findByEmail(String email) throws Exception{
        return userRepository.findByEmail(email).orElseThrow(() -> new Exception(String.format("User with email %s not found", email)));
    }

    public Set<User> getFollowers(User user) {
        return user.getFollowers();
    }


    public boolean toggleSubscription(Long id, Long idTarget) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + id));
        User targetUser = userRepository.findById(idTarget).
                orElseThrow(() -> new UsernameNotFoundException("User not found: " + idTarget));

        if(user.getFollowers().contains(targetUser)) {
            user.getFollowers().remove(targetUser);
            userRepository.save(user);
            return false;
        } else {
            user.getFollowers().add(targetUser);
            userRepository.save(user);
            return true;
        }
    }

}

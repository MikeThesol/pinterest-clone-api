package com.thesol.pinterest_clone.controller;

import com.thesol.pinterest_clone.Services.UserService;
import com.thesol.pinterest_clone.dto.UserDto;
import com.thesol.pinterest_clone.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public String createUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
        return UserDto.createUserDto(userService.getUserById(id));
    }

    @GetMapping("/{id}/followers")
    public Set<User> getFollowers(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
        return userService.getFollowers(userService.getUserById(id));
    }

    @PostMapping("/toggle-subscription/{targetId}")
    public ResponseEntity<Boolean> toggleSubscription(@PathVariable Long targetId) throws ChangeSetPersister.NotFoundException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        User user = userService.getUserByEmail(username);
        boolean isSubscribed = userService.toggleSubscription(user.getId(), targetId);
        return ResponseEntity.ok(isSubscribed);
    }

    @GetMapping("/{id}/subscriptions")
    public Set<User> getSubscriptions(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
        return null;
    }
}

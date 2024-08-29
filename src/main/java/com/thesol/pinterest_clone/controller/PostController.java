package com.thesol.pinterest_clone.controller;

import com.thesol.pinterest_clone.Services.PostService;
import com.thesol.pinterest_clone.Services.UserService;
import com.thesol.pinterest_clone.models.Post;
import com.thesol.pinterest_clone.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final PostRepository postRepository;

    @GetMapping("/search")
    public List<Post> searchPost(@RequestParam String query) {
        return postService.getPostsByTitle(query);
    }

    @GetMapping("/feed")
    public List<Post> feed() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return postService.makeFeedForUser(username);
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @PostMapping("/make")
    public void makePost(@RequestBody Post post) throws ChangeSetPersister.NotFoundException, IOException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        postService.createNewPost(userService.getUserByEmail(username), post);
    }


}

package com.thesol.pinterest_clone.Services;

import com.thesol.pinterest_clone.models.Post;
import com.thesol.pinterest_clone.models.User;
import com.thesol.pinterest_clone.repositories.PostRepository;
import com.thesol.pinterest_clone.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void createNewPost(User user ,Post post) throws IOException {
        post.setAuthor(user);
        post.setCreatedAt(LocalDateTime.now());
        log.info("Post created " + post.getTitle());
        postRepository.save(post);
    }

    public List<Post> makeFeedForUser(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        List<Post> feed = new ArrayList<>();
        for(User following : user.get().getFollowing()) {
            feed.add(postRepository.findById(following.getId()).orElse(null));
        }
        if(!feed.isEmpty()) {
            return feed;
        } else {
            return postRepository.findAll();
        }
    }

    public List<Post> getPostsByTitle(String title) {
        if(title != null) return postRepository.findByTitleContaining(title);
        else return postRepository.findAll();
    }
}

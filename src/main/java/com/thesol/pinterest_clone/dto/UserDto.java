package com.thesol.pinterest_clone.dto;

import com.thesol.pinterest_clone.models.Message;
import com.thesol.pinterest_clone.models.Post;
import com.thesol.pinterest_clone.models.User;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private List<Message> comments;
    private Set<User> followers;
    private Set<User> following;
    private List<Post> posts;
    private List<Post> likedPosts;

    private UserDto() {

    }

    public static UserDto createUserDto(User user) {
        UserDto dto = new UserDto();
        dto.id = user.getId();
        dto.name = user.getName();
        dto.email = user.getEmail();
        dto.comments = user.getComments();
        dto.followers = user.getFollowers();
        dto.following = user.getFollowing();
        dto.posts = user.getPosts();
        dto.likedPosts = user.getLikedPosts();
        return dto;
    }
}

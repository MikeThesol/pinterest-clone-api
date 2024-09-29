package com.thesol.pinterest_clone.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    @OneToMany
    private List<Message> comments;
    @ManyToMany
    private Set<User> followers;
    @ManyToMany
    private Set<User> following;
    @OneToMany
    private List<Post> posts;
    @OneToMany
    private List<Post> likedPosts;
}

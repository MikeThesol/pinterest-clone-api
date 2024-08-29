package com.thesol.pinterest_clone.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue
    private Long id;
    @Lob
    private byte[] photo;
    private String title;
    @ManyToOne
    private User author;
    @OneToMany
    private List<Message> comments;
    private LocalDateTime createdAt;
}

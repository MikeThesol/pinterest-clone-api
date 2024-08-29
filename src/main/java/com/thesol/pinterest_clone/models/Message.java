package com.thesol.pinterest_clone.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "message")
    private String message;
    @OneToOne
    private Post post;
    @OneToOne
    private User sender;
    private LocalDateTime createdAt;
}

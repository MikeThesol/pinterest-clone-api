package com.thesol.pinterest_clone.repositories;

import com.thesol.pinterest_clone.models.Post;
import com.thesol.pinterest_clone.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

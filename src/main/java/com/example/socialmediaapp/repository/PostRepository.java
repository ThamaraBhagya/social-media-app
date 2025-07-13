package com.example.socialmediaapp.repository;

import com.example.socialmediaapp.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthorIdOrderByCreatedAtDesc(Long authorId);

    @Query("SELECT p FROM Post p WHERE p.author.id = :userId OR p.author.id IN " +
            "(SELECT CASE WHEN fr.sender.id = :userId THEN fr.receiver.id " +
            "ELSE fr.sender.id END FROM FriendRequest fr " +
            "WHERE (fr.sender.id = :userId OR fr.receiver.id = :userId) " +
            "AND fr.status = 'ACCEPTED') " +
            "ORDER BY p.createdAt DESC")
    List<Post> findPostsForUserFeed(@Param("userId") Long userId);
}
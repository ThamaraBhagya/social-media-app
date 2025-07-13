package com.example.socialmediaapp.repository;

import com.example.socialmediaapp.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);

    List<Like> findByPostId(Long postId);

    int countByPostId(Long postId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Like l WHERE l.user.id = :userId AND l.post.id = :postId")
    void deleteByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);
}
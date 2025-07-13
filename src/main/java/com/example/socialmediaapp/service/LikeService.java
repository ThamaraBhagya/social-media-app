package com.example.socialmediaapp.service;



import com.example.socialmediaapp.entity.Like;
import com.example.socialmediaapp.entity.Post;
import com.example.socialmediaapp.entity.User;
import com.example.socialmediaapp.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    public boolean toggleLike(User user, Post post) {
        Optional<Like> existingLike = likeRepository.findByUserIdAndPostId(user.getId(), post.getId());

        if (existingLike.isPresent()) {
            likeRepository.deleteByUserIdAndPostId(user.getId(), post.getId());
            return false; // unliked
        } else {
            Like like = new Like(user, post);
            likeRepository.save(like);
            return true; // liked
        }
    }

    public int getLikeCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }

    public boolean isLikedByUser(Long userId, Long postId) {
        return likeRepository.findByUserIdAndPostId(userId, postId).isPresent();
    }
}


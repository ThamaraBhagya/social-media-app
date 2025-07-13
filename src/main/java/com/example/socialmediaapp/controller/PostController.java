package com.example.socialmediaapp.controller;

import com.example.socialmediaapp.entity.Post;
import com.example.socialmediaapp.entity.User;
import com.example.socialmediaapp.service.LikeService;
import com.example.socialmediaapp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private LikeService likeService;

    @PostMapping("/{postId}/like")
    public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable Long postId,
                                                          @AuthenticationPrincipal User currentUser) {
        Optional<Post> postOptional = postService.findById(postId);
        if (!postOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Post post = postOptional.get();
        boolean liked = likeService.toggleLike(currentUser, post);
        int likeCount = likeService.getLikeCount(postId);

        Map<String, Object> response = new HashMap<>();
        response.put("liked", liked);
        response.put("likeCount", likeCount);

        return ResponseEntity.ok(response);
    }
}


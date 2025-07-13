package com.example.socialmediaapp.service;


import com.example.socialmediaapp.dto.PostDto;
import com.example.socialmediaapp.entity.Post;
import com.example.socialmediaapp.entity.User;
import com.example.socialmediaapp.repository.PostRepository;
import com.example.socialmediaapp.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeRepository likeRepository;

    public Post createPost(PostDto postDto, User author) {
        Post post = new Post();
        post.setContent(postDto.getContent());
        post.setAuthor(author);
        return postRepository.save(post);
    }

    public List<PostDto> getPostsForUserFeed(Long userId) {
        List<Post> posts = postRepository.findPostsForUserFeed(userId);
        return posts.stream()
                .map(post -> convertToDto(post, userId))
                .collect(Collectors.toList());
    }

    public List<PostDto> getUserPosts(Long userId) {
        List<Post> posts = postRepository.findByAuthorIdOrderByCreatedAtDesc(userId);
        return posts.stream()
                .map(post -> convertToDto(post, userId))
                .collect(Collectors.toList());
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public PostDto convertToDto(Post post, Long currentUserId) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setAuthorName(post.getAuthor().getFullName());
        dto.setAuthorId(post.getAuthor().getId());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setLikeCount(likeRepository.countByPostId(post.getId()));
        dto.setLikedByCurrentUser(likeRepository.findByUserIdAndPostId(currentUserId, post.getId()).isPresent());
        return dto;
    }
}

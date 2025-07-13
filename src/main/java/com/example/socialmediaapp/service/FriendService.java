package com.example.socialmediaapp.service;

import com.example.socialmediaapp.dto.FriendRequestDto;
import com.example.socialmediaapp.dto.UserDto;
import com.example.socialmediaapp.entity.FriendRequest;
import com.example.socialmediaapp.entity.User;
import com.example.socialmediaapp.repository.FriendRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FriendService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    public FriendRequest sendFriendRequest(User sender, User receiver) {
        // Check if request already exists
        Optional<FriendRequest> existingRequest = friendRequestRepository.findByUsers(sender.getId(), receiver.getId());
        if (existingRequest.isPresent()) {
            throw new RuntimeException("Friend request already exists");
        }

        FriendRequest friendRequest = new FriendRequest(sender, receiver);
        return friendRequestRepository.save(friendRequest);
    }

    public void acceptFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        request.setStatus(FriendRequest.FriendRequestStatus.ACCEPTED);
        friendRequestRepository.save(request);
    }

    public void declineFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        request.setStatus(FriendRequest.FriendRequestStatus.DECLINED);
        friendRequestRepository.save(request);
    }

    public List<FriendRequestDto> getIncomingFriendRequests(Long userId) {
        List<FriendRequest> requests = friendRequestRepository.findByReceiverIdAndStatus(
                userId, FriendRequest.FriendRequestStatus.PENDING);
        return requests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<FriendRequestDto> getOutgoingFriendRequests(Long userId) {
        List<FriendRequest> requests = friendRequestRepository.findBySenderIdAndStatus(
                userId, FriendRequest.FriendRequestStatus.PENDING);
        return requests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public boolean areFriends(Long userId1, Long userId2) {
        Optional<FriendRequest> request = friendRequestRepository.findByUsersAndStatus(
                userId1, userId2, FriendRequest.FriendRequestStatus.ACCEPTED);
        return request.isPresent();
    }

    public boolean hasPendingRequest(Long senderId, Long receiverId) {
        Optional<FriendRequest> request = friendRequestRepository.findByUsers(senderId, receiverId);
        return request.isPresent() && request.get().getStatus() == FriendRequest.FriendRequestStatus.PENDING;
    }

    public UserDto enhanceUserDto(UserDto userDto, Long currentUserId) {
        userDto.setFriend(areFriends(currentUserId, userDto.getId()));
        userDto.setHasPendingRequest(hasPendingRequest(currentUserId, userDto.getId()));
        userDto.setHasReceivedRequest(hasPendingRequest(userDto.getId(), currentUserId));
        return userDto;
    }

    private FriendRequestDto convertToDto(FriendRequest request) {
        FriendRequestDto dto = new FriendRequestDto();
        dto.setId(request.getId());
        dto.setSenderId(request.getSender().getId());
        dto.setSenderName(request.getSender().getFullName());
        dto.setReceiverId(request.getReceiver().getId());
        dto.setReceiverName(request.getReceiver().getFullName());
        dto.setStatus(request.getStatus());
        dto.setCreatedAt(request.getCreatedAt());
        return dto;
    }

}

package com.example.socialmediaapp.repository;

import com.example.socialmediaapp.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByReceiverIdAndStatus(Long receiverId, FriendRequest.FriendRequestStatus status);

    List<FriendRequest> findBySenderIdAndStatus(Long senderId, FriendRequest.FriendRequestStatus status);

    @Query("SELECT fr FROM FriendRequest fr WHERE " +
            "((fr.sender.id = :userId1 AND fr.receiver.id = :userId2) OR " +
            "(fr.sender.id = :userId2 AND fr.receiver.id = :userId1)) AND " +
            "fr.status = :status")
    Optional<FriendRequest> findByUsersAndStatus(@Param("userId1") Long userId1,
                                                 @Param("userId2") Long userId2,
                                                 @Param("status") FriendRequest.FriendRequestStatus status);

    @Query("SELECT fr FROM FriendRequest fr WHERE " +
            "((fr.sender.id = :userId1 AND fr.receiver.id = :userId2) OR " +
            "(fr.sender.id = :userId2 AND fr.receiver.id = :userId1))")
    Optional<FriendRequest> findByUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
}
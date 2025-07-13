package com.example.socialmediaapp.repository;

import com.example.socialmediaapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Page<User> findByIdNot(Long id, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.id IN " +
            "(SELECT CASE WHEN fr.sender.id = :userId THEN fr.receiver.id " +
            "ELSE fr.sender.id END FROM FriendRequest fr " +
            "WHERE (fr.sender.id = :userId OR fr.receiver.id = :userId) " +
            "AND fr.status = 'ACCEPTED')")
    List<User> findFriendsByUserId(@Param("userId") Long userId);
}
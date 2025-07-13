package com.example.socialmediaapp.controller;

import com.example.socialmediaapp.dto.FriendRequestDto;
import com.example.socialmediaapp.dto.UserDto;
import com.example.socialmediaapp.entity.User;
import com.example.socialmediaapp.service.FriendService;
import com.example.socialmediaapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/friends")
public class FriendController {

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @GetMapping
    public String friendsPage(@AuthenticationPrincipal User currentUser,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersPage = userService.findUsersExcluding(currentUser.getId(), pageable);

        List<UserDto> users = usersPage.getContent().stream()
                .map(user -> {
                    UserDto dto = userService.convertToDto(user);
                    return friendService.enhanceUserDto(dto, currentUser.getId());
                })
                .collect(Collectors.toList());

        List<FriendRequestDto> incomingRequests = friendService.getIncomingFriendRequests(currentUser.getId());
        List<FriendRequestDto> outgoingRequests = friendService.getOutgoingFriendRequests(currentUser.getId());
        List<User> friends = userService.findFriends(currentUser.getId());

        model.addAttribute("users", users);
        model.addAttribute("incomingRequests", incomingRequests);
        model.addAttribute("outgoingRequests", outgoingRequests);
        model.addAttribute("friends", friends);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("currentUser", currentUser);

        return "friends";
    }

    @PostMapping("/request/{userId}")
    @ResponseBody
    public ResponseEntity<String> sendFriendRequest(@PathVariable Long userId,
                                                    @AuthenticationPrincipal User currentUser) {
        try {
            Optional<User> receiverOpt = userService.findById(userId);
            if (!receiverOpt.isPresent()) {
                return ResponseEntity.badRequest().body("User not found");
            }

            friendService.sendFriendRequest(currentUser, receiverOpt.get());
            return ResponseEntity.ok("Friend request sent");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/accept/{requestId}")
    @ResponseBody
    public ResponseEntity<String> acceptFriendRequest(@PathVariable Long requestId) {
        try {
            friendService.acceptFriendRequest(requestId);
            return ResponseEntity.ok("Friend request accepted");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/decline/{requestId}")
    @ResponseBody
    public ResponseEntity<String> declineFriendRequest(@PathVariable Long requestId) {
        try {
            friendService.declineFriendRequest(requestId);
            return ResponseEntity.ok("Friend request declined");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

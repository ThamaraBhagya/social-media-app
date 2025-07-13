package com.example.socialmediaapp.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDto {
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private boolean isFriend;
    private boolean hasPendingRequest;
    private boolean hasReceivedRequest;

    // Constructors
    public UserDto() {}

    public UserDto(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isFriend() { return isFriend; }
    public void setFriend(boolean friend) { isFriend = friend; }

    public boolean isHasPendingRequest() { return hasPendingRequest; }
    public void setHasPendingRequest(boolean hasPendingRequest) { this.hasPendingRequest = hasPendingRequest; }

    public boolean isHasReceivedRequest() { return hasReceivedRequest; }
    public void setHasReceivedRequest(boolean hasReceivedRequest) { this.hasReceivedRequest = hasReceivedRequest; }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}

package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.LoginRequestDto;
import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.User;
import com.aicustomersupport.demo.cs.repository.UserRepository;
import com.aicustomersupport.demo.cs.service.interfac.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Response registerUser(User user) {
        Response response = new Response();
        try {
            if (userRepository.existsByEmail(user.getEmail())) {
                return Response.builder()
                        .statusCode(400)
                        .message("Email is already in use")
                        .build();
            }
            User savedUser = userRepository.save(user);
            return Response.builder()
                    .statusCode(200)
                    .message("User registered successfully")
                    .user(savedUser)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error registering user: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response loginUser(LoginRequestDto loginRequest) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
            if (userOptional.isPresent() && userOptional.get().getPassword().equals(loginRequest.getPassword())) {
                return Response.builder()
                        .statusCode(200)
                        .message("Login successful")
                        .user(userOptional.get())
                        .build();
            }
            return Response.builder()
                    .statusCode(400)
                    .message("Invalid email or password")
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error during login: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getUser(Long id) {
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                return Response.builder()
                        .statusCode(200)
                        .message("User retrieved successfully")
                        .user(userOptional.get())
                        .build();
            }
            return Response.builder()
                    .statusCode(404)
                    .message("User not found with id: " + id)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error retrieving user: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return Response.builder()
                    .statusCode(200)
                    .message("Users retrieved successfully")
                    .users(users)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error retrieving users: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response updateUser(User updatedUser, Long id) {
        try {
            Optional<User> existingUserOptional = userRepository.findById(id);
            if (existingUserOptional.isPresent()) {
                User existingUser = existingUserOptional.get();

                if (updatedUser.getName() != null) existingUser.setName(updatedUser.getName());
                if (updatedUser.getEmail() != null) existingUser.setEmail(updatedUser.getEmail());
                if (updatedUser.getRole() != null) existingUser.setRole(updatedUser.getRole());

                User savedUser = userRepository.save(existingUser);
                return Response.builder()
                        .statusCode(200)
                        .message("User updated successfully")
                        .user(savedUser)
                        .build();
            }
            return Response.builder()
                    .statusCode(404)
                    .message("User not found with id: " + id)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error updating user: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response deleteUser(Long id) {
        try {
            if (userRepository.existsById(id)) {
                userRepository.deleteById(id);
                return Response.builder()
                        .statusCode(200)
                        .message("User deleted successfully")
                        .build();
            }
            return Response.builder()
                    .statusCode(404)
                    .message("User not found with id: " + id)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .statusCode(500)
                    .message("Error deleting user: " + e.getMessage())
                    .build();
        }
    }
}
package com.aicustomersupport.demo.cs.service.impl;

import com.aicustomersupport.demo.cs.dto.LoginRequestDto;
import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.User;
import com.aicustomersupport.demo.cs.repository.UserRepository;
import com.aicustomersupport.demo.cs.service.interfac.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                response.setStatusCode(400);
                response.setMessage("Email already registered");
                return response;
            }

            User savedUser = userRepository.save(user);

            response.setStatusCode(200);
            response.setMessage("User registered successfully");
            response.setUser(savedUser);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error while registering user: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response loginUser(LoginRequestDto loginRequest) {

        Response response = new Response();

        try {

            Optional<User> userOptional =
                    userRepository.findByEmail(loginRequest.getEmail());

            if (userOptional.isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("User not found");
                return response;
            }

            User user = userOptional.get();

            if (!user.getPassword().equals(loginRequest.getPassword())) {
                response.setStatusCode(400);
                response.setMessage("Invalid password");
                return response;
            }

            response.setStatusCode(200);
            response.setMessage("Login successful");
            response.setUser(user);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error while logging in: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getUser(Long id) {

        Response response = new Response();

        try {

            Optional<User> userOptional = userRepository.findById(id);

            if (userOptional.isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("User not found");
                return response;
            }

            response.setStatusCode(200);
            response.setUser(userOptional.get());

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error while getting user: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response updateUser(User user, Long id) {

        Response response = new Response();

        try {

            Optional<User> existingUserOptional =
                    userRepository.findById(id);

            if (existingUserOptional.isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("User not found");
                return response;
            }

            User existingUser = existingUserOptional.get();

            if (user.getName() != null) {
                existingUser.setName(user.getName());
            }

            if (user.getEmail() != null) {
                existingUser.setEmail(user.getEmail());
            }

            if (user.getPassword() != null) {
                existingUser.setPassword(user.getPassword());
            }

            if (user.getRole() != null) {
                existingUser.setRole(user.getRole());
            }

            User updatedUser = userRepository.save(existingUser);

            response.setStatusCode(200);
            response.setMessage("User updated successfully");
            response.setUser(updatedUser);

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error while updating user: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response deleteUser(Long id) {

        Response response = new Response();

        try {

            if (!userRepository.existsById(id)) {
                response.setStatusCode(400);
                response.setMessage("User not found");
                return response;
            }

            userRepository.deleteById(id);

            response.setStatusCode(200);
            response.setMessage("User deleted successfully");

        } catch (Exception e) {

            response.setStatusCode(500);
            response.setMessage("Error while deleting user: " + e.getMessage());
        }

        return response;
    }
}

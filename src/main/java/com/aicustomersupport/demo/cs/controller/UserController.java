package com.aicustomersupport.demo.cs.controller;

import com.aicustomersupport.demo.cs.dto.LoginRequestDto;
import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.User;
import com.aicustomersupport.demo.cs.service.interfac.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    public Response registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public Response loginUser(@RequestBody LoginRequestDto loginRequest) {
        return userService.loginUser(loginRequest);
    }

    @GetMapping("/{id}")
    public Response getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping
    public Response getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    public Response updateUser(
            @RequestBody User user,
            @PathVariable Long id) {
        return userService.updateUser(user, id);
    }

    @DeleteMapping("/{id}")
    public Response deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
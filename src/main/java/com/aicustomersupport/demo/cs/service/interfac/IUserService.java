package com.aicustomersupport.demo.cs.service.interfac;

import com.aicustomersupport.demo.cs.dto.LoginRequestDto;
import com.aicustomersupport.demo.cs.dto.Response;
import com.aicustomersupport.demo.cs.model.User;

public interface IUserService {

    Response registerUser(User user);

    Response loginUser(LoginRequestDto loginRequest);

    Response getUser(Long id);

    Response getAllUsers(); // <-- Added

    Response updateUser(User user, Long id);

    Response deleteUser(Long id);
}
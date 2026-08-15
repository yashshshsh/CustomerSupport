package com.aicustomersupport.demo.cs.dto;

import com.aicustomersupport.demo.cs.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String name;

    private String email;

    private Role role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
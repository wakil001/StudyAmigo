package com.StudyPal.Users.User;

import lombok.Data;

import java.util.List;

@Data
public class UserUpdateRequest {
    String name;
    String email;
    List<String> roles;
}

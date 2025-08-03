package com.StudyPal.Users.User;

import jakarta.persistence.Column;
import lombok.Data;


@Data
public class RegisterationRequest {


    private String name;
    private String password;
    private String email;
}

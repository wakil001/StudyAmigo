package com.StudyPal.Users.User;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Data
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name= "username",nullable = false)
    private String username;
    @Column(name="password",nullable = false)
    private String password;
    private String email;
    private String role;
    private String phone;


    @ElementCollection
            @CollectionTable(name="user_flashcard",joinColumns = @JoinColumn(name="user_id"))
            @Column(name = "flashCard_id")
    private List<Integer> flashCards;


    @ElementCollection
            @CollectionTable(name="user_quiz",joinColumns = @JoinColumn(name="user_id"))
            @Column(name="quiz_id")
    private List<Integer> quizzes;


}

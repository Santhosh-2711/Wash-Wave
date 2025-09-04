package com.example.carwash.entity;


import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SecurityQuestionUtil {

    public static String getRandomSecurityQuestion() {
        List<String> questions = Arrays.asList(
            "What is your favorite color?",
            "What was your first pet's name?",
            "What is your mother's maiden name?",
            "What city were you born in?",
            "What was the name of your elementary school?",
            "What is the name of your best friend?",
            "What is your favorite food?",
            "What was the make of your first car?",
            "What is your father's middle name?",
            "What is your favorite movie?",
            "What was your childhood nickname?",
            "What street did you grow up on?",
            "What is the name of your first teacher?",
            "What is your favorite book?",
            "What is your favorite vacation spot?",
            "What was your dream job as a child?",
            "What is your favorite sport?",
            "What is the name of your first employer?",
            "What is your favorite hobby?",
            "What is the name of your first crush?"
        );
        Random rand = new Random();
        return questions.get(rand.nextInt(questions.size()));
    }
}
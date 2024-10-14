package ru.javawebinar.topjava.web;


public class SecurityUtil {

    private static int userId = 1;

    public static void setUserId(int id) {
        userId = id;
    }

    public static int authUserId() {
        return userId;
    }

    public static int authUserCaloriesPerDay() {
        return 2000;
    }
}
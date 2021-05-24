package com.example.diploma;

import java.util.ArrayList;
import java.util.List;

public class User {

    int UserId;
    String FullName;
    String Login;
    String Password;
    String Role;
    List<Patient> Patients;
    List<Doctor> Doctors;

    public User(int userId, String fullName, String login, String password, String role) {
        UserId = userId;
        FullName = fullName;
        Login = login;
        Password = password;
        Role = role;
        Patients =  new ArrayList();
        Doctors = new ArrayList();
    }
}

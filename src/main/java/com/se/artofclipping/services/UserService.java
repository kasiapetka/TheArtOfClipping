package com.se.artofclipping.services;

import com.se.artofclipping.model.User;
import com.se.artofclipping.model.Visit;

import java.util.List;

public interface UserService {

    void changeEmail(User user, String password, String email);

    void changeName(User user, String name);

    boolean changePassword(User user, String oldPassword, String newPassword);

    void changeSurname(User user, String surname);

    List<Visit> getFutureVisits(java.util.List<Visit> visits);
}

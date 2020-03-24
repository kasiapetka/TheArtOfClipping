package com.se.artofclipping.services;

import com.se.artofclipping.model.User;
import com.se.artofclipping.model.Visit;

import java.util.List;

public interface ClientService extends UserService {

    User findUserByEmail(String email);

    List<Visit> listVisits(User client);

    void deleteAccount(User client);

    boolean validatePasswordAndEmail(User user1, User user2);
}

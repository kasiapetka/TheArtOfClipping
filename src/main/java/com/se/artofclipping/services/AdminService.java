package com.se.artofclipping.services;

import com.se.artofclipping.model.User;

import java.util.List;

public interface AdminService extends ClientService {

    void saveHairdresser(User hairdresser);

    boolean delHairdresser(User hairdresser, String adminPassword, String adminEmail);

    boolean changeHdsName(User hairdresser, String adminPassword, String adminEmail, String newName);

    boolean changeHdsSurname(User hairdresser, String adminPassword, String adminEmail, String newSurname);

    boolean changeHdsEmail(User hairdresser, String adminPassword, String adminEmail, String newEmail);

    boolean changeHdsPassword(User hairdresser, String adminPassword, String adminEmail, String newPassword);

    List<User> listHairdressers();

    User findUserByEmail(String email);

    User findUserById(Long id);

    void addDayOff(String day, User hairdresser);
}

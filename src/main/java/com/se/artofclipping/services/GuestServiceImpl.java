package com.se.artofclipping.services;

import com.se.artofclipping.model.Role;
import com.se.artofclipping.model.User;
import com.se.artofclipping.repositories.RoleRepository;
import com.se.artofclipping.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

//@TODO probably will have to split this into more interfaces

@Service
@Primary
public class GuestServiceImpl implements GuestService {

    protected UserRepository userRepository;
    protected RoleRepository roleRepository;
    protected BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public GuestServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepository.findByRole("CUSTOMER");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

    public boolean checkIfAdmin(String email) {
        Role userRole = roleRepository.findByRole("ADMIN");
        User user = userRepository.findByEmail(email);
        if (user.getRoles().contains(userRole)) {
            return true;
        } else return false;
    }

    public boolean checkIfHairdresser(String email) {
        Role userRole = roleRepository.findByRole("EMPLOYEE");
        User user = userRepository.findByEmail(email);
        if (user.getRoles().contains(userRole)) {
            return true;
        } else return false;
    }
}

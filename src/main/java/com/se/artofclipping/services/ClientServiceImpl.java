package com.se.artofclipping.services;

import com.se.artofclipping.model.User;
import com.se.artofclipping.model.Visit;
import com.se.artofclipping.repositories.RoleRepository;
import com.se.artofclipping.repositories.UserRepository;
import com.se.artofclipping.repositories.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

//@TODO probably will have to split this into more interfaces

@Service
@Primary
public class ClientServiceImpl extends UserServiceImpl implements ClientService {

    VisitRepository visitRepository;

    @Autowired
    public ClientServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                             BCryptPasswordEncoder bCryptPasswordEncoder, VisitRepository visitRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.visitRepository = visitRepository;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<Visit> listVisits(User client) {

        List<Visit> visits = new ArrayList<>();
        visitRepository.findByClient(client).iterator().forEachRemaining(visits::add);

        return getFutureVisits(visits);
    }


    @Override
    public boolean validatePasswordAndEmail(User rawUser, User encodedUser) {

        if (rawUser.getEmail().equals(encodedUser.getEmail())
                && bCryptPasswordEncoder.matches(rawUser.getPassword(), encodedUser.getPassword())) {
            return true;
        }

        return false;
    }

    @Transactional
    @Override
    public void deleteAccount(User client) {

        List<Visit> visitsToDelete = new ArrayList<>();
        visitRepository.findByClient(client).iterator().forEachRemaining(visitsToDelete::add);

        for (Visit toDelete : visitsToDelete) {
            visitRepository.delete(toDelete);
        }

        client.setActive(0);
        client.setEmail("deleted");
        client.setName(null);
        client.setPassword(null);
        client.setRoles(null);
        client.setSurname(null);

        userRepository.save(client);
    }
}

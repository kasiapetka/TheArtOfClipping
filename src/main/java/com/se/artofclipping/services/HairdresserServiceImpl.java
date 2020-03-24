package com.se.artofclipping.services;

import com.se.artofclipping.model.User;
import com.se.artofclipping.model.Visit;
import com.se.artofclipping.repositories.RoleRepository;
import com.se.artofclipping.repositories.UserRepository;
import com.se.artofclipping.repositories.VisitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class HairdresserServiceImpl extends UserServiceImpl implements HairdresserService {

    VisitRepository visitRepository;

    @Autowired
    public HairdresserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                                  RoleRepository roleRepository, VisitRepository visitRepository) {
        super(userRepository, bCryptPasswordEncoder, roleRepository);
        this.visitRepository = visitRepository;
    }

    @Override
    public List<Visit> listVisits(User hairdresser) {

        List<Visit> visits = new ArrayList<>();
        visitRepository.findByHairDresser(hairdresser).iterator().forEachRemaining(visits::add);
        return getFutureVisits(visits);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}

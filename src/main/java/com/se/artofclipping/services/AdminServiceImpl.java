package com.se.artofclipping.services;

import com.se.artofclipping.model.Role;
import com.se.artofclipping.model.User;
import com.se.artofclipping.model.Visit;
import com.se.artofclipping.repositories.RoleRepository;
import com.se.artofclipping.repositories.ServiceRepository;
import com.se.artofclipping.repositories.UserRepository;
import com.se.artofclipping.repositories.VisitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service
public class AdminServiceImpl extends ClientServiceImpl implements AdminService {

    ServiceRepository serviceRepository;

    public AdminServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                            RoleRepository roleRepository, VisitRepository visitRepository,
                            ServiceRepository serviceRepository) {
        super(userRepository, roleRepository, bCryptPasswordEncoder, visitRepository);
        this.roleRepository = roleRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public void saveHairdresser(User hairdresser) {
        hairdresser.setPassword(bCryptPasswordEncoder.encode(hairdresser.getPassword()));
        hairdresser.setActive(1);
        Role userRole = roleRepository.findByRole("EMPLOYEE");
        hairdresser.setRoles(new HashSet<>(Arrays.asList(userRole)));
        userRepository.save(hairdresser);
    }

    @Transactional
    @Override
    public boolean delHairdresser(User hairdresser, String adminPassword, String adminEmail) {
        //TODO make successful deletion of hairdressers
        User user = userRepository.findByEmail(adminEmail);
        List<Visit> visitsToDelete = new ArrayList<>();
        visitRepository.findByHairDresser(hairdresser).iterator().forEachRemaining(visitsToDelete::add);

        for (Visit toDelete : visitsToDelete) {
            visitRepository.delete(toDelete);
        }

        if (bCryptPasswordEncoder.matches(adminPassword, user.getPassword())) {
            hairdresser.setActive(0);
            userRepository.save(hairdresser);
            return true;
        }
        return false;
    }

    @Override
    public boolean changeHdsName(User hairdresser, String adminPassword, String adminEmail, String newName) {
        User user = userRepository.findByEmail(adminEmail);

        if (bCryptPasswordEncoder.matches(adminPassword, user.getPassword())) {
            if (!newName.equals("")) {
                hairdresser.setName(newName);
                userRepository.save(hairdresser);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean changeHdsSurname(User hairdresser, String adminPassword, String adminEmail, String newSurname) {
        User user = userRepository.findByEmail(adminEmail);

        if (bCryptPasswordEncoder.matches(adminPassword, user.getPassword())) {
            if (!newSurname.equals("")) {
                hairdresser.setSurname(newSurname);
                userRepository.save(hairdresser);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean changeHdsEmail(User hairdresser, String adminPassword, String adminEmail, String newEmail) {
        User user = userRepository.findByEmail(adminEmail);

        if (bCryptPasswordEncoder.matches(adminPassword, user.getPassword())) {
            if (!newEmail.equals("")) {
                hairdresser.setEmail(newEmail);
                userRepository.save(hairdresser);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean changeHdsPassword(User hairdresser, String adminPassword, String adminEmail, String newPassword) {
        User user = userRepository.findByEmail(adminEmail);

        if (bCryptPasswordEncoder.matches(adminPassword, user.getPassword())) {
            if (!newPassword.equals("")) {
                hairdresser.setPassword(bCryptPasswordEncoder.encode(newPassword));
                userRepository.save(hairdresser);
                return true;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<User> listHairdressers() {

        List<User> hairdressers = new ArrayList<>();
        List<User> users = new ArrayList<>();

        userRepository.findAll().iterator().forEachRemaining(users::add);

        Role userRole = roleRepository.findByRole("EMPLOYEE");

        for (User user : users) {
            if (user.getRoles().equals(new HashSet<>(Arrays.asList(userRole))) && user.getActive() == 1) {
                hairdressers.add(user);
            }
        }

        return hairdressers;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void deleteAccount(User user) {

    }

    @Override
    @Transactional
    public void addDayOff(String day, User hairdresser) {

        Visit visit = new Visit();

        User user = userRepository.findByEmail("Day Off");
        com.se.artofclipping.model.Service dayOffService = serviceRepository.findByName("Day Off");

        List<Visit> visits = visitRepository.findByHairDresser(hairdresser);
        List<Visit> visitsToDelete = new ArrayList<Visit>();
        for(Visit v : visits){
            if(v.getDay().equals(day)){
                visitsToDelete.add(v);
            }
        }
        visitRepository.deleteAll(visitsToDelete);

        visit.setDay(day);
        visit.setClient(user);
        visit.setHairDresser(hairdresser);
        visit.setService(dayOffService);
        visit.setTime("10:00");
        visitRepository.save(visit);

    }


    public User findUserById(Long id) {
        Optional<com.se.artofclipping.model.User> optional = userRepository.findById(id);

        if (!optional.isPresent()) {
            throw new RuntimeException("User Not Found");
        }

        return optional.get();
    }
}

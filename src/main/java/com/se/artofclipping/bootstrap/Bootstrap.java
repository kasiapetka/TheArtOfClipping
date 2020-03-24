package com.se.artofclipping.bootstrap;

import com.se.artofclipping.model.Role;
import com.se.artofclipping.model.Service;
import com.se.artofclipping.model.User;
import com.se.artofclipping.model.Visit;
import com.se.artofclipping.repositories.RoleRepository;
import com.se.artofclipping.repositories.ServiceRepository;
import com.se.artofclipping.repositories.UserRepository;
import com.se.artofclipping.repositories.VisitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

// Goal of this class is to populate databae with some "testing" data
// Works only if profile h2 is selected if not only admin will be added
// and database will be initialised via data.sql

@Slf4j
@Component
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final VisitRepository visitRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Bootstrap(RoleRepository roleRepository, UserRepository userRepository, ServiceRepository serviceRepository,
                     VisitRepository visitRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.visitRepository = visitRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    private List<User> hairdressers = new ArrayList<>();
    private List<User> customers = new ArrayList<>();

    List<Service> services = new ArrayList<>();

    @Value("${spring.database.name}")
    private String dataBase;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println(dataBase);
        if (dataBase.equals("h2")) {
            log.debug("Loading bootstrap data.");
            roleRepository.saveAll(getRoles());
            userRepository.saveAll(getAdmins());
            userRepository.saveAll(getCustomers());
            userRepository.saveAll(getHairdressers());
            serviceRepository.saveAll(getServices());
            visitRepository.saveAll(getVisits());
        }

        //@Todo couldnt find way to encrypt password for pure sql script
        if (dataBase.equals("postgres")) {
            User user = userRepository.findByEmail("admin@admin.com");
            if (user == null) {
                userRepository.saveAll(getAdmins());
            }
        }
    }

    private List<Visit> getVisits() {

        List<Visit> visits = new ArrayList<>();

        Visit visit = new Visit();
        visit.setIsAvailable(true);
        visit.setDay("24-1-2020");
        visit.setTime("10:30");
        visit.setHairDresser(hairdressers.get(0));
        visit.setClient(customers.get(0));
        visit.setService(services.get(0));

        visits.add(visit);

        visit = new Visit();
        visit.setIsAvailable(true);
        visit.setDay("28-1-2020");
        visit.setTime("11:30");
        visit.setHairDresser(hairdressers.get(0));
        visit.setClient(customers.get(0));
        visit.setService(services.get(0));

        visits.add(visit);


        visit = new Visit();
        visit.setIsAvailable(true);
        visit.setDay("24-1-2020");
        visit.setTime("12:30");
        visit.setHairDresser(hairdressers.get(0));
        visit.setClient(customers.get(0));
        visit.setService(services.get(0));

        visits.add(visit);


        visit = new Visit();
        visit.setIsAvailable(true);
        visit.setDay("24-1-2020");
        visit.setTime("13:30");
        visit.setHairDresser(hairdressers.get(1));
        visit.setClient(customers.get(0));
        visit.setService(services.get(0));

        visits.add(visit);


        visit = new Visit();
        visit.setIsAvailable(true);
        visit.setDay("24-1-2020");
        visit.setTime("14:30");
        visit.setHairDresser(hairdressers.get(1));
        visit.setClient(customers.get(0));
        visit.setService(services.get(0));

        visits.add(visit);


        visit = new Visit();
        visit.setIsAvailable(true);
        visit.setDay("24-1-2020");
        visit.setTime("15:30");
        visit.setHairDresser(hairdressers.get(1));
        visit.setClient(customers.get(0));
        visit.setService(services.get(0));

        visits.add(visit);

        return visits;
    }

    private List<Role> getRoles() {
        List<Role> roles = new ArrayList<>();

        Role adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setRole("ADMIN");

        roles.add(adminRole);

        Role employeeRole = new Role();
        employeeRole.setRole("EMPLOYEE");
        employeeRole.setId(2L);

        roles.add(employeeRole);

        Role customerRole = new Role();
        customerRole.setRole("CUSTOMER");
        customerRole.setId(3L);

        roles.add(customerRole);

        return roles;
    }

    private List<User> getAdmins() {
        List<User> admins = new ArrayList<>();

        User admin = new User();
        admin.setEmail("admin@admin.com");
        admin.setPassword(bCryptPasswordEncoder.encode("admin"));
        admin.setActive(1);
        admin.setId(1L);
        admin.setName("AdminName");
        admin.setSurname("AdminSurname");

        Role userRole = roleRepository.findByRole("ADMIN");
        admin.setRoles(new HashSet<>(Arrays.asList(userRole)));

        admins.add(admin);

        return admins;
    }

    private List<User> getCustomers() {


        Role userRole = roleRepository.findByRole("CUSTOMER");
        User customer = new User();
        customer.setEmail("aaa@aaa.com");
        customer.setPassword(bCryptPasswordEncoder.encode("aaa"));
        customer.setActive(1);
        customer.setName("aaa");
        customer.setSurname("aaa");

        customer.setRoles(new HashSet<>(Arrays.asList(userRole)));

        customers.add(customer);

        customer = new User();
        customer.setEmail("Day Off");
        customer.setPassword(bCryptPasswordEncoder.encode("Day Off"));
        customer.setActive(0);
        customer.setName("Day");
        customer.setSurname("Off");

        customer.setRoles(new HashSet<>(Arrays.asList(userRole)));

        customers.add(customer);

        customer = new User();
        customer.setEmail("bbb@bbb.com");
        customer.setPassword(bCryptPasswordEncoder.encode("bbb"));
        customer.setActive(1);
        customer.setName("bbb");
        customer.setSurname("bbb");

        customer.setRoles(new HashSet<>(Arrays.asList(userRole)));

        customers.add(customer);

        customer = new User();
        customer.setEmail("ccc@ccc.com");
        customer.setPassword(bCryptPasswordEncoder.encode("ccc"));
        customer.setActive(1);
        customer.setName("ccc");
        customer.setSurname("ccc");

        customer.setRoles(new HashSet<>(Arrays.asList(userRole)));

        customers.add(customer);

        customer = new User();
        customer.setEmail("ddd@ddd.com");
        customer.setPassword(bCryptPasswordEncoder.encode("ddd"));
        customer.setActive(1);
        customer.setName("ddd");
        customer.setSurname("ddd");

        customer.setRoles(new HashSet<>(Arrays.asList(userRole)));

        customers.add(customer);

        return customers;
    }

    private List<User> getHairdressers() {

        User hairdresser = new User();

        hairdresser.setEmail("hhh@hhh.com");
        hairdresser.setPassword(bCryptPasswordEncoder.encode("hhh"));
        hairdresser.setActive(1);
        hairdresser.setName("hhh");
        hairdresser.setSurname("hhh");

        Role userRole = roleRepository.findByRole("EMPLOYEE");
        hairdresser.setRoles(new HashSet<>(Arrays.asList(userRole)));

        hairdressers.add(hairdresser);

        hairdresser = new User();
        hairdresser.setEmail("ggg@ggg.com");
        hairdresser.setPassword(bCryptPasswordEncoder.encode("ggg"));
        hairdresser.setActive(1);
        hairdresser.setName("ggg");
        hairdresser.setSurname("ggg");

        hairdresser.setRoles(new HashSet<>(Arrays.asList(userRole)));

        hairdressers.add(hairdresser);

        hairdresser = new User();
        hairdresser.setEmail("jjj@jjj.com");
        hairdresser.setPassword(bCryptPasswordEncoder.encode("jjj"));
        hairdresser.setActive(1);
        hairdresser.setName("jjj");
        hairdresser.setSurname("jjj");

        hairdresser.setRoles(new HashSet<>(Arrays.asList(userRole)));

        hairdressers.add(hairdresser);

        hairdresser = new User();
        hairdresser.setEmail("kkk@kkk.com");
        hairdresser.setPassword(bCryptPasswordEncoder.encode("kkk"));
        hairdresser.setActive(1);
        hairdresser.setName("kkk");
        hairdresser.setSurname("kkk");

        hairdresser.setRoles(new HashSet<>(Arrays.asList(userRole)));

        hairdressers.add(hairdresser);

        hairdresser = new User();
        hairdresser.setEmail("lll@lll.com");
        hairdresser.setPassword(bCryptPasswordEncoder.encode("lll"));
        hairdresser.setActive(1);
        hairdresser.setName("lll");
        hairdresser.setSurname("lll");

        hairdresser.setRoles(new HashSet<>(Arrays.asList(userRole)));

        hairdressers.add(hairdresser);

        hairdresser = new User();
        hairdresser.setEmail("zzz@zzz.com");
        hairdresser.setPassword(bCryptPasswordEncoder.encode("zzz"));
        hairdresser.setActive(1);
        hairdresser.setName("zzz");
        hairdresser.setSurname("zzz");

        hairdresser.setRoles(new HashSet<>(Arrays.asList(userRole)));

        hairdressers.add(hairdresser);

        return hairdressers;
    }

    private List<Service> getServices() {

        // Male services
        Service service = new Service();
        service.setName("Clipper Cut");
        service.setIsActive(true);
        service.setDurationMinutes(30);
        service.setPrice(18D);
        service.setType(Character.toUpperCase('M'));
        services.add(service);

        service = new Service();
        service.setName("Scissor Cut");
        service.setIsActive(true);
        service.setDurationMinutes(30);
        service.setPrice(20D);
        service.setType(Character.toUpperCase('M'));
        services.add(service);

        service = new Service();
        service.setName("Cut & Beard Trim/Wash");
        service.setIsActive(true);
        service.setDurationMinutes(60);
        service.setPrice(25D);
        service.setType(Character.toUpperCase('M'));
        services.add(service);

        service = new Service();
        service.setName("Cut & Beard Trim & Wash");
        service.setIsActive(true);
        service.setDurationMinutes(60);
        service.setPrice(30D);
        service.setType(Character.toUpperCase('M'));
        services.add(service);

        service = new Service();
        service.setName("Cut & Style");
        service.setIsActive(true);
        service.setDurationMinutes(90);
        service.setPrice(60D);
        service.setType(Character.toUpperCase('M'));
        services.add(service);

        service = new Service();
        service.setName("Cut & Wash & Style");
        service.setIsActive(true);
        service.setDurationMinutes(90);
        service.setPrice(70D);
        service.setType(Character.toUpperCase('M'));
        services.add(service);

        // Female services
        service = new Service();
        service.setName("Bang Trim");
        service.setIsActive(true);
        service.setDurationMinutes(30);
        service.setPrice(10D);
        service.setType(Character.toUpperCase('F'));
        services.add(service);

        service = new Service();
        service.setName("Cut Only");
        service.setIsActive(true);
        service.setDurationMinutes(30);
        service.setPrice(20D);
        service.setType(Character.toUpperCase('F'));
        services.add(service);

        service = new Service();
        service.setName("Wash & Style");
        service.setIsActive(true);
        service.setDurationMinutes(60);
        service.setPrice(35D);
        service.setType(Character.toUpperCase('F'));
        services.add(service);

        service = new Service();

        service.setName("Wash & Cut");
        service.setIsActive(true);
        service.setDurationMinutes(60);
        service.setPrice(40D);
        service.setType(Character.toUpperCase('F'));
        services.add(service);

        service = new Service();
        service.setName("Wash & Cut & Style");
        service.setIsActive(true);
        service.setDurationMinutes(90);
        service.setPrice(70D);
        service.setType(Character.toUpperCase('F'));
        services.add(service);

        service = new Service();
        service.setName("Wash & Cut & Color");
        service.setIsActive(true);
        service.setDurationMinutes(90);
        service.setPrice(90D);
        service.setType(Character.toUpperCase('F'));
        services.add(service);

        service = new Service();
        service.setName("Day Off");
        service.setIsActive(false);
        service.setDurationMinutes(480);
        service.setPrice(0.0D);
        service.setType('Q');
        services.add(service);

        return services;
    }
}
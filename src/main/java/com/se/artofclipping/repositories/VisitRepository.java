package com.se.artofclipping.repositories;

import com.se.artofclipping.model.Service;
import com.se.artofclipping.model.User;
import com.se.artofclipping.model.Visit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends CrudRepository<Visit, Long> {

    List<Visit> findByDay(String day);
    List<Visit> findByClient(User client);
    List<Visit> findByHairDresser(User hairdresser);
    List<Visit> findByServiceAndHairDresser(Service service, User hairdresser);
}

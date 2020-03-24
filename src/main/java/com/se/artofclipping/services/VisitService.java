package com.se.artofclipping.services;

import com.se.artofclipping.model.Service;
import com.se.artofclipping.model.User;
import com.se.artofclipping.model.Visit;

import java.util.List;

public interface VisitService {

    void addNewVisit(Visit visit);

    void addNewVisit(User user, User hd, Long serviceId, String day, String time);

    void deleteVisit(Visit visit);

    List<Visit> findByDay(String day);

    Visit findById(Long id);

    List<Visit> findByHds(User hds);

    List<Visit> listVisits();

    List<Visit> findByServiceAndHairdresser(Service service, User hairdresser);

    void deleteAll(List<Visit> listToDelete);
}

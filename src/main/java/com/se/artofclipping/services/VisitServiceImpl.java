package com.se.artofclipping.services;

import com.se.artofclipping.model.User;
import com.se.artofclipping.model.Visit;
import com.se.artofclipping.repositories.VisitRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VisitServiceImpl implements VisitService {

    VisitRepository visitRepository;

    public VisitServiceImpl(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    @Override
    public List<Visit> listVisits() {

        List<Visit> visits = new ArrayList<>();
        visitRepository.findAll().iterator().forEachRemaining(visits::add);

        return visits;
    }

    @Override
    public List<Visit> findByDay(String day) {

        List<Visit> visits = new ArrayList<>();
        visitRepository.findByDay(day).iterator().forEachRemaining(visits::add);

        return visits;
    }

    @Override
    public List<Visit> findByServiceAndHairdresser(com.se.artofclipping.model.Service service, User hairdresser) {
        List<Visit> visits = new ArrayList<>();
        visitRepository.findByServiceAndHairDresser(service, hairdresser).iterator().forEachRemaining(visits::add);

        return visits;
    }

    @Transactional
    @Override
    public void deleteAll(List<Visit> listToDelete) {
        visitRepository.deleteAll(listToDelete);
    }

    @Override
    public Visit findById(Long id) {
        Optional<com.se.artofclipping.model.Visit> optional = visitRepository.findById(id);

        if (!optional.isPresent()) {
            throw new RuntimeException("Visit Not Found");
        }

        return optional.get();
    }

    @Override
    public List<Visit> findByHds(User hds) {

        List<Visit> visits = new ArrayList<>();
        visitRepository.findByHairDresser(hds).iterator().forEachRemaining(visits::add);
        return visits;
    }

    @Override
    public void addNewVisit(User user, User hd, Long serviceId, String day, String time) {

    }

    @Override
    public void addNewVisit(Visit visit) {
        visitRepository.save(visit);
    }

    @Override
    public void deleteVisit(Visit visit) {
        visitRepository.delete(visit);
    }
}

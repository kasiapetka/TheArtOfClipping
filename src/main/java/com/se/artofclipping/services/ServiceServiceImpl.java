package com.se.artofclipping.services;

import com.se.artofclipping.model.Service;
import com.se.artofclipping.repositories.ServiceRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@Todo sort these function I mean change their location in file
@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {

    ServiceRepository serviceRepository;

    public ServiceServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<Service> listService(Character type) {
        List<Service> services = new ArrayList<>();
        serviceRepository.findAllByType(type).iterator().forEachRemaining(services::add);
        return services;
    }

    @Override
    public List<Service> listServiceByTypeAndavailability(Character type, Boolean isAvailable) {
        List<Service> services = new ArrayList<>();
        serviceRepository.findByTypeAndIsActive(type, isAvailable).iterator().forEachRemaining(services::add);
        return services;
    }

    @Override
    public Service findById(Long id) {
        Optional<Service> serviceOptional = serviceRepository.findById(id);

        if (!serviceOptional.isPresent()) {
            throw new RuntimeException("Service Not Found");
        }

        return serviceOptional.get();
    }

    @Override
    public void addService(Service toAdd) {
        serviceRepository.save(toAdd);
    }

    @Override
    public Service findByName(String name) {
        return serviceRepository.findByName(name);
    }

    @Override
    public void changeDuration(Service service, Integer newDuration) {
        Service serviceToUpdate = findById(service.getId());

        serviceToUpdate.setDurationMinutes(newDuration);
        serviceRepository.save(serviceToUpdate);
    }

    @Override
    public void changeType(Service service, Character newType) {
        Service serviceToUpdate = findById(service.getId());

        serviceToUpdate.setType(newType);
        serviceRepository.save(serviceToUpdate);
    }

    @Override
    public void changeName(Service service, String newName) {
        Service serviceToUpdate = findById(service.getId());
        serviceToUpdate.setName(newName);
        serviceRepository.save(serviceToUpdate);
    }

    @Override
    public void changePrice(Service service, Double newPrice) {
        Service serviceToUpdate = findById(service.getId());

        serviceToUpdate.setPrice(newPrice);
        serviceRepository.save(serviceToUpdate);
    }

    @Override
    public void changeIsActive(Service service, boolean activity) {
        Service serviceToUpdate = findById(service.getId());

        serviceToUpdate.setIsActive(activity);
        serviceRepository.save(serviceToUpdate);
    }

    @Transactional
    @Override
    public void deprecateService(Long id) {
        Service serviceToDeprecate = findById(id);
        serviceToDeprecate.setIsActive(false);
        serviceToDeprecate.setType('Q');
        serviceRepository.save(serviceToDeprecate);
    }
}

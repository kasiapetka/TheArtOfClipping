package com.se.artofclipping.repositories;

import com.se.artofclipping.model.Service;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends CrudRepository<Service, Long> {

    List<Service> findAllByType(Character type);
    List<Service> findByTypeAndIsActive(Character type, Boolean isActive);
    Service findByName(String name);
}

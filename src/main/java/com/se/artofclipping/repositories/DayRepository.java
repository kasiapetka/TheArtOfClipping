package com.se.artofclipping.repositories;

import com.se.artofclipping.model.Day;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayRepository extends CrudRepository<Day, Long> {
}

package com.se.artofclipping.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

//@Todo not used may be usefull in refactoring
@Data
@Entity
@Table(name = "days")
public class Day {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    private Date date;
    private Boolean workday;
}

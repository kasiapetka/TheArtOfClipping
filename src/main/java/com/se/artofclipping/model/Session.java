package com.se.artofclipping.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

//@Todo not used may be usefull in refactoring
@Data
@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    private Date begginingTime;

    @ManyToOne
    private Day day;
}

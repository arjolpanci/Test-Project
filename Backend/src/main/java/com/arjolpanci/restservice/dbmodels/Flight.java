package com.arjolpanci.restservice.dbmodels;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@NoArgsConstructor
public class Flight {

    public static final String ECONOMY_CLASS = "Economy";
    public static final String BUSINESS_CLASS = "Business";
    public static final String FIRST_CLASS = "First";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column private String departure;
    @Column private String destination;
    @Column private Date departure_time;
    @Column(name="class") private String flight_class;

    public Flight(String departure, String destination, Date departure_time, String flightClass) {
        this.departure = departure;
        this.destination = destination;
        this.departure_time = departure_time;
        this.flight_class = flightClass;
    }
}

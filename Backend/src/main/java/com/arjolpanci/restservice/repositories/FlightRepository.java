package com.arjolpanci.restservice.repositories;

import com.arjolpanci.restservice.dbmodels.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Optional;

@Repository
public interface FlightRepository extends PagingAndSortingRepository<Flight, Long> {
    Optional<Flight> findById(Long id);

    @Query(value = "SELECT * FROM flight WHERE flight.destination LIKE ?1 AND flight.departure LIKE ?2 AND flight.departure_time >= ?3 AND flight.departure_time <= ?4",
            countQuery = "SELECT count(*) FROM flight WHERE flight.destination LIKE ?1 AND flight.departure LIKE ?2 AND flight.departure_time >= ?3 AND flight.departure_time <= ?4",
            nativeQuery = true)
    Page<Flight> getFlightsByFilters(String destinationSearchTerm, String departureSearchTerm,
                                     Date beforeDate, Date afterDate, Pageable pageable);
}

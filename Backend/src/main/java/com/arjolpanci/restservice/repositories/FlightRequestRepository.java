package com.arjolpanci.restservice.repositories;

import com.arjolpanci.restservice.dbmodels.FlightRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Optional;

@Repository
public interface FlightRequestRepository extends PagingAndSortingRepository<FlightRequest, Long> {
    Optional<FlightRequest> findById(Long id);

    @Query(value = "SELECT * FROM flight_request",
            countQuery = "SELECT count(*) FROM flight_request",
            nativeQuery = true)
    Page<FlightRequest> getPaginatedFlightRequests(Pageable pageable);

    @Query(value = "SELECT * FROM flight_request WHERE user_id=?1",
            countQuery = "SELECT count(*) FROM flight_request WHERE user_id=?1",
            nativeQuery = true)
    Page<FlightRequest> getPaginatedFlightRequestsForUser(Long userId, Pageable pageable);

    @Query(value = "SELECT * FROM flight_request WHERE user_id=?2 AND created_at < ?1",
            countQuery = "SELECT count(*) FROM flight_request WHERE user_id=?2 AND created_at < ?1",
            nativeQuery = true)
    Page<FlightRequest> getHistoryForUser(Date today, Long userId, Pageable pageable);
}

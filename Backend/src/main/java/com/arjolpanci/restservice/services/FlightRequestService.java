package com.arjolpanci.restservice.services;

import com.arjolpanci.restservice.dbmodels.Flight;
import com.arjolpanci.restservice.dbmodels.FlightRequest;
import com.arjolpanci.restservice.dbmodels.User;
import com.arjolpanci.restservice.httpmodels.IncomingPageRequest;
import com.arjolpanci.restservice.repositories.FlightRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;

@Service
public class FlightRequestService {

    @Autowired
    private FlightRequestRepository flightRequestRepository;

    public FlightRequest findById(Long Id) throws Exception {
        Optional<FlightRequest> optFLightRequest = flightRequestRepository.findById(Id);
        if(optFLightRequest.isPresent()) {
            return optFLightRequest.get();
        }
        throw new Exception("There is no flight reques with the request id!");
    }

    public Page<FlightRequest> getPaginatedFlightRequests(IncomingPageRequest request) {
        Pageable pageData = PageRequest.of(request.getPageNumber(), request.getPageSize(), Sort.by("created_at").descending());
        return flightRequestRepository.getPaginatedFlightRequests(pageData);
    }

    public Page<FlightRequest> getPaginatedFlightRequestsForUser(Long userId, IncomingPageRequest request) {
        Pageable pageData = PageRequest.of(request.getPageNumber(), request.getPageSize(), Sort.by("created_at").descending());
        return flightRequestRepository.getPaginatedFlightRequestsForUser(userId, pageData);
    }

    public Page<FlightRequest> getHistoryForUser(Long userId, IncomingPageRequest request) {
        Pageable pageData = PageRequest.of(request.getPageNumber(), request.getPageSize(), Sort.by("created_at").descending());
        long milis = System.currentTimeMillis();
        Date now = new Date(milis);
        return flightRequestRepository.getHistoryForUser(now,userId, pageData);
    }

    public void saveFlightRequest(Long flightRequestId, String status, String rejection_reason, User user, Flight flight) throws Exception {
        if(flightRequestId != null) {
            FlightRequest flightRequest = this.findById(flightRequestId);
            if(status != null) flightRequest.setStatus(status);
            if(rejection_reason != null) flightRequest.setRejection_reason(rejection_reason);
        }else{
            FlightRequest flightRequest = new FlightRequest();
            flightRequest.setStatus(status);
            flightRequest.setRejection_reason(rejection_reason);
            flightRequest.setUser(user);
            flightRequest.setFlight(flight);
            flightRequestRepository.save(flightRequest);
        }
    }

    public void updateFlightRequest(FlightRequest flightRequest) {
        flightRequestRepository.save(flightRequest);
    }

}

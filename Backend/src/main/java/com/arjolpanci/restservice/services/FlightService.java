package com.arjolpanci.restservice.services;

import com.arjolpanci.restservice.dbmodels.Flight;
import com.arjolpanci.restservice.httpmodels.IncomingPageRequest;
import com.arjolpanci.restservice.repositories.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    public Flight findById(Long Id) throws Exception {
        Optional<Flight> optFLight = flightRepository.findById(Id);
        if(optFLight.isPresent()) {
            return optFLight.get();
        }
        throw new Exception("There is no flight with the request id!");
    }

    public Page<Flight> getFlightsByFilter(IncomingPageRequest request, String destinationSearchString,
                                           String departureSearchString, Date beforeDate, Date afterDate) {
        Pageable pageData = PageRequest.of(request.getPageNumber(), request.getPageSize());
        if(destinationSearchString == null){
            destinationSearchString = "";
        }else{
            destinationSearchString = "%" + destinationSearchString + "%";
        }
        if(departureSearchString == null){
            departureSearchString = "";
        }else{
            departureSearchString = "%" + departureSearchString + "%";
        }
        return flightRepository.getFlightsByFilters(destinationSearchString, departureSearchString, beforeDate, afterDate, pageData);
    }

    public void saveFlight(String departure, String destination, Date departure_time, String flightClass) {
        Flight flight = new Flight(departure, destination, departure_time, flightClass);
        flightRepository.save(flight);
    }

}

package com.arjolpanci.restservice.controllers;

import com.arjolpanci.restservice.dbmodels.Flight;
import com.arjolpanci.restservice.dbmodels.FlightRequest;
import com.arjolpanci.restservice.dbmodels.User;
import com.arjolpanci.restservice.httpmodels.FlightFilteredRequest;
import com.arjolpanci.restservice.httpmodels.NewFlightRequest;
import com.arjolpanci.restservice.httpmodels.IncomingPageRequest;
import com.arjolpanci.restservice.services.FlightRequestService;
import com.arjolpanci.restservice.services.FlightService;
import com.arjolpanci.restservice.services.UsersService;
import com.arjolpanci.restservice.util.InsufficientAccessException;
import com.arjolpanci.restservice.util.JwtUtils;
import com.arjolpanci.restservice.util.ResponseManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/flights")
public class FlightController {

    private static final Logger LOG = LogManager.getLogger(FlightController.class);

    @Autowired
    ResponseManager responseManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UsersService usersService;

    @Autowired
    FlightService flightService;

    @Autowired
    FlightRequestService flightRequestService;

    @PostMapping("/create")
    public ResponseManager.ResponseObject<String> createFlight(@RequestBody Flight request) {
        try {
            long milis = System.currentTimeMillis();
            Date now = new Date(milis);
            if(request.getDeparture().equals(request.getDestination())) {
                throw new Exception("Departure cannot be the same as the destination!");
            }
            if(request.getDeparture_time().compareTo(now) < 0) {
                throw new Exception("Flight date cannot be in the past!");
            }
            if(request.getFlight_class().equals(Flight.FIRST_CLASS)
                    && request.getFlight_class().equals(Flight.BUSINESS_CLASS)
                    && request.getFlight_class().equals(Flight.ECONOMY_CLASS)) {
                throw new Exception("Incorrect flight class provided!");
            }
            flightService.saveFlight(request.getDeparture(), request.getDestination(),
                    request.getDeparture_time(), request.getFlight_class());
            return responseManager.getFormattedResponse(HttpStatus.OK, "Flight has been created!", null);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return responseManager.getFormattedResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @PostMapping
    public ResponseManager.ResponseObject<List<Flight>> getFlightsByFilter(@RequestBody FlightFilteredRequest request) {
        List<Flight> flights = flightService.getFlightsByFilter(request.getIncomingPageRequest(), request.getDestinationSearchString(),
                request.getDepartureSearchString(), request.getBeforeDate(), request.getAfterDate()).getContent();
        return responseManager.getFormattedResponse(HttpStatus.OK, "Flights have been filtered!", flights);
    }

    @GetMapping("/{flightId}")
    public ResponseManager.ResponseObject<Flight> getFlight(@PathVariable(value="flightId") Long flightId){
        try {
            Flight flight = flightService.findById(flightId);
            return responseManager.getFormattedResponse(HttpStatus.OK, "Flight data retrieved succesfully!", flight);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return responseManager.getFormattedResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @PostMapping("/requests")
    public ResponseManager.ResponseObject<List<FlightRequest>> getPaginatedFlightRequests(@RequestHeader("Authorization") String authHeader,
                                                                                          @RequestBody IncomingPageRequest request) {
        try {
            String token = authHeader.split(" ")[1];
            jwtUtils.verifyUserAuthority(null, token, User.ROLE_SUPERVISOR, null);
            List<FlightRequest> flightRequests = flightRequestService.getPaginatedFlightRequests(request).getContent();
            return responseManager.getFormattedResponse(HttpStatus.OK, "Flight Requests retrieved successfully!", flightRequests);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return responseManager.getFormattedResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        }
    }

    @PostMapping("/requests/users/{userId}")
    public ResponseManager.ResponseObject<List<FlightRequest>> getPaginatedFlightRequestsForUser(@RequestHeader("Authorization") String authHeader,
                                                                                                 @PathVariable(value="userId") Long userId,
                                                                                                 @RequestBody IncomingPageRequest request) {
        try {
            String token = authHeader.split(" ")[1];
            User user = usersService.findUserById(userId);
            jwtUtils.verifyUserAuthority(user, token, User.ROLE_USER, userId);
            List<FlightRequest> flightRequests = flightRequestService.getPaginatedFlightRequestsForUser(userId, request).getContent();
            return responseManager.getFormattedResponse(HttpStatus.OK, "Flight Requests retrieved successfully!", flightRequests);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return responseManager.getFormattedResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @PostMapping("/requests/users/{userId}/history")
    public ResponseManager.ResponseObject<List<FlightRequest>> getRequestHistoryForUser(@RequestHeader("Authorization") String authHeader,
                                                                                                 @PathVariable(value="userId") Long userId,
                                                                                                 @RequestBody IncomingPageRequest request) {
        try {
            String token = authHeader.split(" ")[1];
            User user = usersService.findUserById(userId);
            jwtUtils.verifyUserAuthority(user, token, User.ROLE_USER, userId);
            List<FlightRequest> flightRequests = flightRequestService.getHistoryForUser(userId, request).getContent();
            return responseManager.getFormattedResponse(HttpStatus.OK, "Flight Requests retrieved successfully!", flightRequests);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return responseManager.getFormattedResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @GetMapping("/requests/{requestId}")
    public ResponseManager.ResponseObject<FlightRequest> getFlightRequest(@RequestHeader("Authorization") String authHeader, @PathVariable(value="requestId") Long requestId){
        try {
            String token = authHeader.split(" ")[1];
            FlightRequest flightRequest = flightRequestService.findById(requestId);
            jwtUtils.verifyUserAuthority(null, token, User.ROLE_USER, flightRequest.getUser().getId());
            return responseManager.getFormattedResponse(HttpStatus.OK, "FlightRequest data retrieved succesfully!", flightRequest);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return responseManager.getFormattedResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @PostMapping("/requests/{userId}/create")
    public ResponseManager.ResponseObject<String> createNewFlightRequest(@RequestHeader("Authorization") String authHeader,
                                                                         @PathVariable(value="userId") Long userId,
                                                                         @RequestBody NewFlightRequest request) {
        try {
            String token = authHeader.split(" ")[1];
            User user = usersService.findUserById(userId);
            Flight flight = flightService.findById(request.getFlightId());
            jwtUtils.verifyUserAuthority(user, token, User.ROLE_USER, userId);
            flightRequestService.saveFlightRequest(null, FlightRequest.REQUEST_STATUS_PENDING, null, user, flight);
            return responseManager.getFormattedResponse(HttpStatus.OK, "Flight request has been registered", null);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return responseManager.getFormattedResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @PatchMapping("/requests/{reqId}/update")
    public ResponseManager.ResponseObject<String> updateFlightRequest(@RequestHeader("Authorization") String authHeader,
                                                                         @PathVariable(value="reqId") Long reqId,
                                                                         @RequestBody NewFlightRequest request) {
        try {
            String token = authHeader.split(" ")[1];
            jwtUtils.verifyUserAuthority(null, token, User.ROLE_SUPERVISOR, null);
            FlightRequest flightRequest = flightRequestService.findById(reqId);
            if(request.getRejection_reason() != null) flightRequest.setRejection_reason(request.getRejection_reason());
            if(request.getStatus() != null) flightRequest.setStatus(request.getStatus());
            flightRequestService.updateFlightRequest(flightRequest);
            return responseManager.getFormattedResponse(HttpStatus.OK, "Flight request has been updated", null);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return responseManager.getFormattedResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
}

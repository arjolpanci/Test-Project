package com.arjolpanci.restservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Testing responses for some flight data requests")
public class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void should_list_all_flights_related_to_filters() throws Exception {
        long milis = System.currentTimeMillis();
        Date now = new Date(milis);

        Map<String,Object> pageRequest = new HashMap<>();
        Map<String,Object> body = new HashMap<>();
        pageRequest.put("pageNumber", 0);
        pageRequest.put("pageSize", 5);
        body.put("beforeDate", now);
        body.put("afterDate", now);
        body.put("incomingPageRequest", pageRequest);
        ObjectMapper objectMap = new ObjectMapper();

        this.mockMvc.perform(post("/flights").contentType(MediaType.APPLICATION_JSON)
            .content(objectMap.writeValueAsString(body))
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.statusCode", is("OK")))
            .andExpect(jsonPath("$", hasKey("data")))
            .andExpect(jsonPath("$", hasKey("statusCode")))
            .andExpect(jsonPath("$", hasKey("message")));
    }

    @Test
    public void should_list_flight_requests_paginated() throws Exception {
        Map<String,Object> body = new HashMap<>();
        body.put("pageSize", 5);
        body.put("pageNumber", 0);
        ObjectMapper objectMap = new ObjectMapper();

        this.mockMvc.perform(post("/flights/requests").contentType(MediaType.APPLICATION_JSON)
            .content(objectMap.writeValueAsString(body))
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.statusCode", is("OK")))
            .andExpect(jsonPath("$", hasKey("data")))
            .andExpect(jsonPath("$", hasKey("statusCode")))
            .andExpect(jsonPath("$", hasKey("message")));
    }

    @Test
    public void should_list_flight_requests_for_user_paginated() throws Exception {
        Map<String,Object> body = new HashMap<>();
        body.put("pageSize", 5);
        body.put("pageNumber", 0);
        ObjectMapper objectMap = new ObjectMapper();

        this.mockMvc.perform(post("/flights/requests/users/2").contentType(MediaType.APPLICATION_JSON)
                .content(objectMap.writeValueAsString(body))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode", is("OK")))
                .andExpect(jsonPath("$", hasKey("data")))
                .andExpect(jsonPath("$", hasKey("statusCode")))
                .andExpect(jsonPath("$", hasKey("message")));
    }

}

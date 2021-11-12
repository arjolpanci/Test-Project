package com.arjolpanci.restservice;

import static org.assertj.core.api.Assertions.assertThat;

import com.arjolpanci.restservice.controllers.AuthenticationController;
import com.arjolpanci.restservice.controllers.FlightController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ContextTest {

    @Autowired
    private AuthenticationController authenticationController;
    @Autowired
    private FlightController flightController;

    @Test
    public void contextLoads() throws Exception {
        assertThat(authenticationController).isNotNull();
        assertThat(flightController).isNotNull();
    }
}


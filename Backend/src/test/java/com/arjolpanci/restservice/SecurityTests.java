package com.arjolpanci.restservice;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testing possible security breaches")
public class SecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void should_not_give_access_without_authorization() throws Exception {
        Map<String,Object> body = new HashMap<>();
        body.put("pageSize", 5);
        body.put("pageNumber", 0);
        ObjectMapper objectMap = new ObjectMapper();

        mockMvc.perform(get("/flights/1"))
                .andDo(print()).andExpect(status().is4xxClientError());
        mockMvc.perform(get("/flights/requests/1"))
                .andDo(print()).andExpect(status().is4xxClientError());
        mockMvc.perform(post("/flights/requests").contentType(MediaType.APPLICATION_JSON)
                .content(objectMap.writeValueAsString(body))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
        mockMvc.perform(post("/flights/requests/users/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMap.writeValueAsString(body))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
        mockMvc.perform(post("/flights/requests/users/1/history").contentType(MediaType.APPLICATION_JSON)
                .content(objectMap.writeValueAsString(body))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }


}

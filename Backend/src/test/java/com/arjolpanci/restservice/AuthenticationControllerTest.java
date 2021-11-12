package com.arjolpanci.restservice;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Testing responses for authentication / registration")
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void should_list_users_with_pagination_on_admin_call() throws Exception {
        Map<String,Object> body = new HashMap<>();
        body.put("pageSize", 5);
        body.put("pageNumber", 0);
        ObjectMapper objectMap = new ObjectMapper();

        this.mockMvc.perform(post("/auth/users").contentType(MediaType.APPLICATION_JSON)
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
    public void should_successfully_register_user() throws Exception {
        Map<String,Object> body = new HashMap<>();
        body.put("username", "test");
        body.put("password", "test");
        body.put("email", "email");
        ObjectMapper objectMap = new ObjectMapper();

        this.mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
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

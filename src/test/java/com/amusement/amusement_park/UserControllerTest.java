package com.amusement.amusement_park;

import com.amusement.amusement_park.entity.User;
import com.amusement.amusement_park.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll(); // clean users before each test
    }

    @Test
    void testRegisterUser() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("email", "testuser@example.com");
        request.put("password", "password123");
        request.put("role", "MEMBER");

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        assertThat(userRepository.findByEmail("testuser@example.com")).isPresent();
    }

    @Test
    void testLoginFailsIfNotVerified() throws Exception {
        User user = new User();
        user.setEmail("loginuser@example.com");
        user.setPassword("$2a$10$7MwrK6eo9MQZtqukAB6eu.SwGdrIxQTSQIO1Yu6CvFXhxXaUftSGa"); // "password123"
        user.setRole("MEMBER");
        user.setVerified(false);
        userRepository.save(user);

        Map<String, String> request = new HashMap<>();
        request.put("email", "loginuser@example.com");
        request.put("password", "password123");

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}

package com.equipo01.featureflag.featureflag.controller.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.equipo01.featureflag.featureflag.dto.request.LoginRequestDto;
import com.equipo01.featureflag.featureflag.dto.request.UserRequestDTO;
import com.equipo01.featureflag.featureflag.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerImpTest {
    @Value("${api.auth}")
    private String authEndpoint;

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService userService;
    private ObjectMapper objectMapper;
    private String tokenExpected;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tokenExpected = "token";
        objectMapper = new ObjectMapper();
    }

    @Test
    void testRegisterUser() throws Exception {
        UserRequestDTO userRequestDTO = UserRequestDTO.builder()
                .username("test")
                .email("test@gmail.com")
                .password("testtest")
                .build();
        when(userService.registerUser(any())).thenReturn(tokenExpected);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post(authEndpoint + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        String tokenResult = result.getResponse().getContentAsString();
        assertEquals(tokenExpected, tokenResult);
    }

    @Test
    void testLongUser() throws Exception {
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .username("test")
                .password("testtest")
                .build();

        when(userService.loginUser(any())).thenReturn(tokenExpected);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post(authEndpoint + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String tokenResult = result.getResponse().getContentAsString();
        assertEquals(tokenExpected, tokenResult);
    }
}

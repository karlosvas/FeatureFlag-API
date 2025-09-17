package com.equipo01.featureflag.featureflag.controller.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.equipo01.featureflag.featureflag.dto.UserDTO;
import com.equipo01.featureflag.featureflag.dto.request.LoginRequestDto;
import com.equipo01.featureflag.featureflag.dto.request.UserRequestDTO;
import com.equipo01.featureflag.featureflag.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("staging")
class UserControllerImpTest {
  @Value("${api.auth}")
  private String authEndpoint;

  @Autowired private MockMvc mockMvc;
  @MockitoBean private UserService userService;
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
    String password = RandomString.make() + "#.5A";
    UserRequestDTO userRequestDTO =
        UserRequestDTO.builder()
            .username("test")
            .email("test@gmail.com")
            .password(password)
            .build();
    when(userService.registerUser(any())).thenReturn(tokenExpected);
    MvcResult result =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(authEndpoint + "/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userRequestDTO)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();
    String tokenResult = result.getResponse().getContentAsString();
    assertEquals(tokenExpected, tokenResult);
  }

  @Test
  void testLongUser() throws Exception {
    LoginRequestDto loginRequestDto =
        LoginRequestDto.builder().username("test").password("testtest").build();

    when(userService.loginUser(any())).thenReturn(tokenExpected);
    MvcResult result =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(authEndpoint + "/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequestDto)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
    String tokenResult = result.getResponse().getContentAsString();
    assertEquals(tokenExpected, tokenResult);
  }

    @Test
  void testHealthCheck() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get(authEndpoint + "/health"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("OK"));
  }

  @Test
  void testRegisterAdmin() throws Exception {
    String password = RandomString.make() + "#.5A";
    UserRequestDTO userRequestDTO =
        UserRequestDTO.builder()
            .username("admin")
            .email("admin@gmail.com")
            .password(password)
            .build();
    when(userService.registerAdmin(any())).thenReturn(tokenExpected);
    
    MvcResult result =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post(authEndpoint + "/register/admin")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userRequestDTO)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();
    String tokenResult = result.getResponse().getContentAsString();
    assertEquals(tokenExpected, tokenResult);
  }

 @Test
void testGetAllUsers() throws Exception {
  List<UserDTO> users = Arrays.asList(
      UserDTO.builder().id(UUID.randomUUID()).username("user1").email("user1@test.com").build(),
      UserDTO.builder().id(UUID.randomUUID()).username("user2").email("user2@test.com").build()
  );
  when(userService.getAllUsers()).thenReturn(users);

  mockMvc
      .perform(MockMvcRequestBuilders.get(authEndpoint + "/users")
          .with(user("testuser").roles("USER")))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].username", is("user1")))
      .andExpect(MockMvcResultMatchers.jsonPath("$[1].username", is("user2")));
}

 @Test
void testGetUserByEmail() throws Exception {
  UserDTO user = UserDTO.builder()
      .id(UUID.randomUUID())
      .username("testuser")
      .email("test@gmail.com")
      .build();
  when(userService.getUserByEmail("test@gmail.com")).thenReturn(user);

  mockMvc
      .perform(MockMvcRequestBuilders.get(authEndpoint + "/user/test@gmail.com")
          .with(user("testuser").roles("USER")))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.username", is("testuser")))
      .andExpect(MockMvcResultMatchers.jsonPath("$.email", is("test@gmail.com")));
}
  
  @Test
  void testDeleteUser() throws Exception {
    UUID userId = UUID.randomUUID();
    doNothing().when(userService).deleteUser(userId);

    mockMvc
        .perform(MockMvcRequestBuilders.delete(authEndpoint + "/" + userId.toString())
            .with(user("admin").roles("ADMIN")))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
    
    verify(userService, times(1)).deleteUser(userId);
  }

  @Test
  void testCheckPermissionTest() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get(authEndpoint + "/test")
            .with(user("admin").roles("ADMIN")))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("Test permission ok"));
  }

  @Test
  void testCheckPermissionTestWithoutAdminRole_shouldReturnForbidden() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get(authEndpoint + "/test")
            .with(user("user").roles("USER")))
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  void testGetAllUsersWithoutAuthentication_shouldReturnUnauthorized() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get(authEndpoint + "/users"))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  void testDeleteUserWithoutAdminRole_shouldReturnForbidden() throws Exception {
    UUID userId = UUID.randomUUID();
    
    mockMvc
        .perform(MockMvcRequestBuilders.delete(authEndpoint + "/" + userId.toString())
            .with(user("user").roles("USER")))
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }
}

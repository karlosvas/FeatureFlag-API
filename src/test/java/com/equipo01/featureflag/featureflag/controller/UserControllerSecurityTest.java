package com.equipo01.featureflag.featureflag.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("staging")
class UserControllerSecurityTest {

  @Autowired private MockMvc mockMvc;

  @Value("${api.security}")
  private String securityEndpoint;

  @WithMockUser(
      username = "admin",
      password = "adminPassword123",
      roles = {"USER"})
  @Test
  void whenUserWithoutRole_thenForbidden403() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get(securityEndpoint + "/test"))
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }
}

package com.equipo01.featureflag.featureflag.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("staging")
class FeatureControllerSecurityTest {

  @Autowired private MockMvc mockMvc;

  @Value("${api.features}")
  private String featuresEndpoint;

  @Value("${api.configurations}")
  private String featuresConfigEndpoint;

  @Test
  void whenNoToken_thenUnauthorized401() throws Exception {
    MvcResult result =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get(featuresEndpoint).param("page", "0").param("size", "10"))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized())
            .andReturn();
    String stringResult = result.getResponse().getContentAsString();
    System.out.println(stringResult);
  }

  @WithMockUser(
      username = "admin",
      password = "adminPassword123",
      roles = {"USER"})
  @Test
  void whenUserWithoutRole_thenForbidden403() throws Exception {

    mockMvc
        .perform(MockMvcRequestBuilders.get(featuresConfigEndpoint + "/test"))
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }
}

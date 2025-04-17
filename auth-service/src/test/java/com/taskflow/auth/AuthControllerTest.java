package com.taskflow.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AuthServiceApplication.class)
@AutoConfigureMockMvc
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void shouldReturnTokenForValidCredentials() throws Exception {
		String json = "{ \"username\": \"evandro\", \"password\": \"123456\" }";

		mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk());
	}

	@Test
	public void shouldReturnUnauthorizedForInvalidCredentials() throws Exception {
		String json = "{ \"username\": \"wrong\", \"password\": \"bad\" }";

		mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isUnauthorized());
	}
}
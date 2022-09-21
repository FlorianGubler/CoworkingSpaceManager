package com.github.floriangubler.coworkspacemgr;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.floriangubler.coworkspacemgr.entity.BookingEntityReq;
import com.github.floriangubler.coworkspacemgr.entity.MemberDTO;
import com.github.floriangubler.coworkspacemgr.entity.TokenResponse;
import lombok.val;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CoworkspaceUserIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private static String EMAIL = "test@xyz.ch";
	private static String PW = "test123";

	private static String REFRESHTOKEN;

	@Test
	@Order(1)
	public void register() throws Exception {
		MemberDTO memberDTO = new MemberDTO();
		memberDTO.setEmail(EMAIL);
		memberDTO.setPassword(PW);
		memberDTO.setFirstname("test");
		memberDTO.setLastname("test");
		var response = mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(memberDTO)))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn();

		TokenResponse tokenres = objectMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<>() {});
		REFRESHTOKEN = tokenres.getRefreshToken();
	}

	@Test
	@Order(2)
	public void gettokenByUserPw() throws Exception {

		mockMvc.perform(post("/api/auth/token?grant_type=password&email="+ EMAIL + "&password="+ PW))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn();
	}

	@Test
	@Order(3)
	public void gettokenByRefreshToken() throws Exception {

		mockMvc.perform(post("/api/auth/token?grant_type=refresh_token&refresh_token="+ REFRESHTOKEN))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn();
	}

	@Test
	@Order(4)
	public void getbookingserror() throws Exception {

		val response = mockMvc.perform(get("/api/bookings"))
				.andExpect(status().isUnauthorized())
				.andDo(print())
				.andReturn();
	}

	@Test
	@Order(5)
	public void getmemberserror() throws Exception {

		val response = mockMvc.perform(get("/api/members"))
				.andExpect(status().isUnauthorized())
				.andDo(print())
				.andReturn();
	}

}
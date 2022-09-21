package com.github.floriangubler.coworkspacemgr;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.floriangubler.coworkspacemgr.entity.BookingEntityReq;
import com.github.floriangubler.coworkspacemgr.security.JwtServiceHMAC;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CoworkspaceAdminIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private static JwtServiceHMAC jwtService;

	@Autowired
	private ObjectMapper objectMapper;

	private static String accesstoken;

	@BeforeAll
	public static void jwtauth(){
		accesstoken = jwtService.createNewJWT(UUID.randomUUID().toString(), "9135f12e-1b66-4ee6-bbae-df37303cc154", "admin", List.of("ADMIN"));
	}


	@Test
	public void getbookings() throws Exception {

		val response = mockMvc.perform(get("/api/bookings").header("Authorization", "Bearer " + accesstoken))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn();

		List<BookingEntityReq> bookings = objectMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<>() {});

		assertEquals(4, bookings.size());
	}

}
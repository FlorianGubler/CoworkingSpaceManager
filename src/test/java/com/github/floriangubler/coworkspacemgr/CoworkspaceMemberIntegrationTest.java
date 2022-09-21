package com.github.floriangubler.coworkspacemgr;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.floriangubler.coworkspacemgr.entity.BookingEntity;
import com.github.floriangubler.coworkspacemgr.entity.BookingEntityReq;
import com.github.floriangubler.coworkspacemgr.entity.BookingStatus;
import com.github.floriangubler.coworkspacemgr.entity.BookingTime;
import com.github.floriangubler.coworkspacemgr.security.JwtServiceHMAC;
import lombok.val;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CoworkspaceMemberIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private static String accesstoken;
	private static UUID bookingid;

	private static UUID userid = UUID.fromString("9135f11e-1b66-4ee6-bbae-df37303cc154");

	@BeforeAll
	public static void jwtauth(@Autowired JwtServiceHMAC jwtService){
		accesstoken = jwtService.createNewJWT(UUID.randomUUID().toString(), userid.toString(), "user", new ArrayList<>());
	}

	@Test
	@Order(1)
	public void getbookings() throws Exception {

		val response = mockMvc.perform(get("/api/bookings").header("Authorization", "Bearer " + accesstoken))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn();

		List<BookingEntity> bookings = objectMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<>() {});

		assertEquals(4, bookings.size());
	}

	@Test
	@Order(2)
	public void getmybookings() throws Exception {

		val response = mockMvc.perform(get("/api/bookings/?onlymy=true").header("Authorization", "Bearer " + accesstoken))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn();

		List<BookingEntity> bookings = objectMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<>() {});

		assertEquals(1, bookings.size());
	}

	@Test
	@Order(3)
	public void createbooking() throws Exception {
		BookingEntityReq bookingreq = new BookingEntityReq();
		bookingreq.setMemberId(userid);
		bookingreq.setDate(Date.from(Instant.now()));
		bookingreq.setTime(BookingTime.AFTERNOON);
		val response = mockMvc.perform(post("/api/bookings").header("Authorization", "Bearer " + accesstoken).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(bookingreq)))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn();

		BookingEntity booking = objectMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<>() {});
		bookingid = booking.getId();

		assertNull(booking.getMember()); //Anonymisiation
		assertEquals(booking.getTime(), BookingTime.AFTERNOON);
		assertEquals(booking.getStatus(), BookingStatus.ORDERED);
	}

	@Test
	@Order(4)
	public void deletebooking() throws Exception {
		mockMvc.perform(delete("/api/bookings/" + bookingid.toString()).header("Authorization", "Bearer " + accesstoken))
				.andExpect(status().isNoContent())
				.andDo(print())
				.andReturn();
	}

	@Test
	@Order(5)
	public void deletebookingerror() throws Exception {
		mockMvc.perform(delete("/api/bookings/" + "b8160463-01a0-4c7a-bd46-5b2716dbe4c1").header("Authorization", "Bearer " + accesstoken))
				.andExpect(status().isNotFound())
				.andDo(print())
				.andReturn();
	}

}
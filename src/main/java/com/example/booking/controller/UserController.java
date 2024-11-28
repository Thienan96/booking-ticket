package com.example.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.booking.base.constant.CONST_API;
import com.example.booking.dto.base.ActionResult;
import com.example.booking.dto.base.ResponseBuilder;
import com.example.booking.dto.indto.BookingTicketInDto;
import com.example.booking.dto.indto.CreateUserDto;
import com.example.booking.dto.indto.KeyPairInInDto;
import com.example.booking.dto.indto.UserLoginInDto;
import com.example.booking.service.UserService;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("all")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(CONST_API.API_USER_PREFIX)
@RestController
public class UserController {
    private final ResponseBuilder responseBuilder;
    private final UserService userService;

	@GetMapping(value = CONST_API.BOOKING_TICKET)
	public ResponseEntity addTicket(@RequestBody BookingTicketInDto input) {
		ActionResult result = userService.booking(input);
		return responseBuilder.build(result);
	}

	@GetMapping(value = CONST_API.PUBLIC)
	public String login() {
		return "PUBLICCCCCCC";
	}
	// @PreAuthorize("hasAnyRole('ADMIN')")
	@RolesAllowed({"ADMIN"})
	@GetMapping(value = CONST_API.API_ADMIN)
	public String admin() {
		return "ADMINNNNNN";
	}
	// @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	// @RolesAllowed({"USER", "ADMIN"})
	@GetMapping(value = CONST_API.GET_USER_INFO)
	public ResponseEntity user() {
		ActionResult result = userService.getCurrentUserInfor();
		return responseBuilder.build(result);
	}

	@PostMapping(value = CONST_API.CREATE_USER)
	public ResponseEntity createUser(@RequestBody CreateUserDto input) {
		ActionResult result = userService.createUser(input);
		return responseBuilder.build(result);
	}

	@PostMapping(value = CONST_API.LOGIN)
	public ResponseEntity login(@RequestBody UserLoginInDto input) {
		ActionResult result = userService.login(input);
		return responseBuilder.build(result);
	}
	@PostMapping(value = CONST_API.REFRESH_TOKEN)
	public ResponseEntity login(@RequestBody KeyPairInInDto input) {
		ActionResult result = userService.refreshToken(input);
		return responseBuilder.build(result);
	}

	@GetMapping(value = CONST_API.TEST_USER)
	public ResponseEntity testUser() {
		ActionResult result = userService.testUser();
		return responseBuilder.build(result);
	}
}

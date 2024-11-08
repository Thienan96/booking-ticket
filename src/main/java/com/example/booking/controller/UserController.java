package com.example.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.booking.base.constant.API;
import com.example.booking.dto.base.ActionResult;
import com.example.booking.dto.base.ResponseBuilder;
import com.example.booking.dto.indto.BookingTicketInDto;
import com.example.booking.service.UserService;

import lombok.RequiredArgsConstructor;

@SuppressWarnings("all")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(API.API_USER_PREFIX)
@RestController
public class UserController {
    private final ResponseBuilder responseBuilder;
    private final UserService userService;

	@PostMapping(value = API.BOOKING_TICKET)
	public ResponseEntity addTicket(@RequestBody BookingTicketInDto input) {
		ActionResult result = userService.booking(input);
		return responseBuilder.build(result);
	}
}

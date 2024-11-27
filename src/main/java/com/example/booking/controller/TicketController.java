package com.example.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.booking.base.constant.CONST_API;
import com.example.booking.base.constant.ReturnCodeEnum;
import com.example.booking.dto.base.ActionResult;
import com.example.booking.dto.base.ResponseBuilder;
import com.example.booking.dto.indto.TicketInDto;
import com.example.booking.service.TicketService;

import lombok.RequiredArgsConstructor;

@SuppressWarnings("all")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(CONST_API.API_TICKET_PREFIX)
@RestController
public class TicketController {
    private final ResponseBuilder responseBuilder;
    private final TicketService ticketService;

	@PostMapping(value = CONST_API.ADD_TICKET)
	public ResponseEntity addTicket(@RequestBody TicketInDto input) {
		ActionResult result = ticketService.addTicket(input);
		return responseBuilder.build(result);
	}

	@GetMapping(value = CONST_API.TEST_REDIS)
	public ResponseEntity testRedist() {
		ActionResult result = ticketService.testRedis();
		return responseBuilder.build(result);
	}
}

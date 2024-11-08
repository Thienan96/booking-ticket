package com.example.booking.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.booking.base.constant.API;
import com.example.booking.base.constant.ReturnCodeEnum;
import com.example.booking.dto.base.ActionResult;
import com.example.booking.dto.base.ResponseBuilder;
import com.example.booking.dto.indto.BookingTicketInDto;
import com.example.booking.dto.indto.TicketInDto;
import com.example.booking.service.TicketService;
import com.example.booking.service.TicketCodeService;

import lombok.RequiredArgsConstructor;

@SuppressWarnings("all")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(API.API_TICKET_PREFIX)
@RestController
public class TicketCodeController {
    private final ResponseBuilder responseBuilder;
    private final TicketCodeService ticketCodeService;

	@PostMapping(value = API.GENERATE_TICKET_CODE)
	public ResponseEntity addTicket(@RequestBody BookingTicketInDto input) {
		ActionResult result = ticketCodeService.genTicketCode(input);
		return responseBuilder.build(result);
	}
}

package com.example.booking.service;

import com.example.booking.dto.base.ActionResult;
import com.example.booking.dto.indto.TicketInDto;

@SuppressWarnings("all")
public interface TicketService {
	ActionResult addTicket(TicketInDto ticketInDto);

	ActionResult testRedis();

}

package com.example.booking.service;

import java.util.List;

import com.example.booking.dto.base.ActionResult;
import com.example.booking.dto.indto.BookingTicketInDto;
import com.example.booking.dto.indto.TicketInDto;

@SuppressWarnings("all")
public interface TicketCodeService {
	ActionResult genTicketCode(BookingTicketInDto input);
}

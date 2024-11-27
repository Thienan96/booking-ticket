package com.example.booking.service;

import java.util.List;

import com.example.booking.dto.base.ActionResult;
import com.example.booking.dto.indto.BookingTicketInDto;
import com.example.booking.dto.indto.CreateUserDto;
import com.example.booking.dto.indto.TicketInDto;
import com.example.booking.dto.indto.UserLoginInDto;

@SuppressWarnings("all")
public interface UserService {
	ActionResult booking(BookingTicketInDto input);

	ActionResult getCurrentUserInfor ();

	ActionResult createUser (CreateUserDto input);

	ActionResult login (UserLoginInDto input);

	ActionResult testUser ();



}

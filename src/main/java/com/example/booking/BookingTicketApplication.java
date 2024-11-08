package com.example.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BookingTicketApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingTicketApplication.class, args);
	}

}

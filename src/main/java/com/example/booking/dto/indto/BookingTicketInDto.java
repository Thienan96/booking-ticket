package com.example.booking.dto.indto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingTicketInDto implements Serializable{
    private static final long serialVersionUID = 1L;

    private Long ticketId;
    private Integer amount;
}

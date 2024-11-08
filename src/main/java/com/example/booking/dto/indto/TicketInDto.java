package com.example.booking.dto.indto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketInDto implements Serializable{
    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private Date validDate;
}

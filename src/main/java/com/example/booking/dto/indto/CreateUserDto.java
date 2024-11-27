package com.example.booking.dto.indto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDto implements Serializable{
    private static final long serialVersionUID = 1L;

    private String name;
    private String password;
    private String email;
    private List<Long> roles;
}

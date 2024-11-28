package com.example.booking.dto.outdto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse implements Serializable {
    private final String accessToken;
    private final String refreshToken;
    private final Long time_to_live_seconds;
}

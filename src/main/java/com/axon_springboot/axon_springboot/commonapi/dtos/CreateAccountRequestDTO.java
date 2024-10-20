package com.axon_springboot.axon_springboot.commonapi.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateAccountRequestDTO {
    private Double initialBalance;
    private String currency;
}

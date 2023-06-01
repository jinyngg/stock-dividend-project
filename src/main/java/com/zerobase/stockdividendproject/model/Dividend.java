package com.zerobase.stockdividendproject.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class Dividend {

    private LocalDateTime date;
    private String dividend;

}

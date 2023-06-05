package com.zerobase.stockdividendproject.exception;

import com.zerobase.stockdividendproject.Type.ErrorCode;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockDividendException extends RuntimeException{

    private int statusCode;
    private ErrorCode errorCode;
    private String errorMessage;

    public StockDividendException(ErrorCode errorCode) {
        this.statusCode = errorCode.getStatus();
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}

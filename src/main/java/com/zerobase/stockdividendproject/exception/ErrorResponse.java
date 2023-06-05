package com.zerobase.stockdividendproject.exception;

import com.zerobase.stockdividendproject.Type.ErrorCode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

    private int statusCode;
    private ErrorCode errorCode;
    private String errorMessage;
}

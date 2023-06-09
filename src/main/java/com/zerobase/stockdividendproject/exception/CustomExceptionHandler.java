package com.zerobase.stockdividendproject.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.zerobase.stockdividendproject.Type.ErrorCode.INTERNAL_SERVER_ERROR;


@Slf4j
@ControllerAdvice // 필터와 비슷하게 컨트롤러 밖에서 동작, 어드바이스 => 컨트롤러 레이어와 더 가까이 존재
public class CustomExceptionHandler {

//    @ExceptionHandler(AbstractException.class) // AbstractException이 발생한 경우 에러 핸들링
//    protected ResponseEntity<ErrorResponse> handleCustomException(AbstractException e) {
//        ErrorResponse errorResponse = ErrorResponse.builder()
////                                                    .code(e.getStatusCode())
//                                                    .message(e.getMessage())
//                                                    .build();
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.resolve(e.getStatusCode()));
//    }

    // ErrorCode로 Exception 관리
    @ExceptionHandler(StockDividendException.class)
    public ResponseEntity<ErrorResponse> handleAccountException(StockDividendException e){
        log.error("{} is occurred.", e.getErrorCode());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(e.getStatusCode())
                .errorCode(e.getErrorCode())
                .errorMessage(e.getErrorMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.resolve(errorResponse.getStatusCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e){
        log.error("Exception is occurred.", e);

        ErrorResponse errorResponse = new ErrorResponse(
                INTERNAL_SERVER_ERROR.getStatus()
                , INTERNAL_SERVER_ERROR
                , INTERNAL_SERVER_ERROR.getDescription());

        return new ResponseEntity<>(errorResponse, HttpStatus.resolve(errorResponse.getStatusCode()));
    }
}

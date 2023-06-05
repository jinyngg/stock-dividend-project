package com.zerobase.stockdividendproject.Type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "내부 서버 오류가 발생하였습니다.")
    ,USER_ALREADY_EXISTS(HttpStatus.CONFLICT.value(), "이미 존재하는 사용자명입니다.")
    , TICKER_ALREADY_EXISTS(HttpStatus.CONFLICT.value(), "이미 TICKER가 존재합니다.")
    , TICKER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "TICKER를 찾을 수 없습니다.")
    , ID_NOT_EXISTS(HttpStatus.NOT_FOUND.value(), "존재하지 않는 ID 입니다.")
    , PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다.")
    , UNEXPECTED_MONTH(HttpStatus.BAD_REQUEST.value(), "올바르지 않은 월(Month)을 입력했습니다.")
    , SCRAP_TICKER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "SCRAP TICKER를 찾을 수 없습니다.")
    , COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 회사명 입니다.")
    ;

    private final int status;
    private final String description;
}

package com.zerobase.stockdividendproject.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Company {

    /*
    Entity, Model을 따로 생성하는 이유
    =>
    Entity는 DB와 직접적으로 매핑
    즉, 서비스 코드 내부에서 데이터 주고받기 위한 용도로 사용하거나 내용을 변경하는 로직을 작성하면 역할을 벗어난다고 본다.
     */

    private String ticker;
    private String name;

}

package com.zerobase.stockdividendproject.model;

import com.zerobase.stockdividendproject.persist.entity.MemberEntity;
import lombok.Data;

import java.util.List;

public class Auth {

    @Data
    public static class SignIn {
        private String username;
        private String password;
    }

    @Data
    public static class SignUp {
        private String username;
        private String password;
        private List<String> roles; // 내부 로직 처리(ex) 일반 회원, 관리자 회원의 권한)

        public MemberEntity toEntity() {
            return MemberEntity.builder()
                    .username(this.username)
                    .password(this.password)
                    .roles(this.roles)
                    .build();
        }
    }

}

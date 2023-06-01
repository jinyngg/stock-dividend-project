package com.zerobase.stockdividendproject.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/company")
public class CompanyController {

    // 검색 자동 완성
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword) {

        return null;
    }

    // 회사 리스트 조회
    @GetMapping
    public ResponseEntity<?> searchCompany() {
        return null;
    }

    // 배당금 저장
    @PostMapping
    public ResponseEntity<?> addCompany() {
        return null;
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCompany() {
        return null;
    }
}

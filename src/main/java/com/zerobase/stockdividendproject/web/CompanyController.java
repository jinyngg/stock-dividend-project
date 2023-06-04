package com.zerobase.stockdividendproject.web;

import com.zerobase.stockdividendproject.model.Company;
import com.zerobase.stockdividendproject.persist.entity.CompanyEntity;
import com.zerobase.stockdividendproject.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    // 검색 자동 완성
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
//        var result = this.companyService.autocomplete(keyword);
        var result = this.companyService.getCompanyNameByKeyword(keyword);

        return ResponseEntity.ok(result);
    }

    /**
     * 회사 리스트 조회
     * @return
     */
    @GetMapping
    public ResponseEntity<?> searchCompany(final Pageable pageable) {
        Page<CompanyEntity> companies = this.companyService.getAllCompany(pageable);
        return ResponseEntity.ok(companies);
    }

    /**
     * 회사 및 배당금 정보 추가
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<?> addCompany(@RequestBody Company request) {
        String ticker = request.getTicker().trim();
        if (ObjectUtils.isEmpty(ticker)) {
            throw new RuntimeException("ticker is empty");
        }

        Company company = this.companyService.save(ticker);
        this.companyService.addAutocompleteKeyword(company.getName());

        return ResponseEntity.ok(company);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCompany() {
        return null;
    }
}

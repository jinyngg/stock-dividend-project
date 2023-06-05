package com.zerobase.stockdividendproject.controller;

import com.zerobase.stockdividendproject.exception.StockDividendException;
import com.zerobase.stockdividendproject.model.CompanyDto;
import com.zerobase.stockdividendproject.Type.CacheKey;
import com.zerobase.stockdividendproject.entity.Company;
import com.zerobase.stockdividendproject.service.CompanyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import static com.zerobase.stockdividendproject.Type.ErrorCode.TICKER_NOT_FOUND;

@Slf4j
@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final CacheManager redisCacheManager;

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
    @PreAuthorize("hasRole('READ')")
    public ResponseEntity<?> searchCompany(final Pageable pageable) {
        Page<Company> companies = this.companyService.getAllCompany(pageable);
        return ResponseEntity.ok(companies);
    }

    /**
     * 회사 및 배당금 정보 추가
     * @param request
     * @return
     */
    @PostMapping
    @PreAuthorize("hasRole('WRITE')") // 쓰기 권한이 있는 유저만 API 호출 가능
    public ResponseEntity<?> addCompany(@RequestBody CompanyDto request) {
        String ticker = request.getTicker().trim();
        if (ObjectUtils.isEmpty(ticker)) {
//            throw new RuntimeException("ticker is empty");

            log.error("ticker is empty");
            throw new StockDividendException(TICKER_NOT_FOUND);
        }

        CompanyDto companyDto = this.companyService.save(ticker);
        this.companyService.addAutocompleteKeyword(companyDto.getName());

        return ResponseEntity.ok(companyDto);
    }

    @DeleteMapping("/{ticker}")
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> deleteCompany(@PathVariable String ticker) {
        String companyName = this.companyService.deleteCompany(ticker);
        
        // 캐쉬 정보에서 제거
        this.clearFinanceCache(companyName);

        return ResponseEntity.ok(companyName);
    }

    public void clearFinanceCache(String companyName) {
        this.redisCacheManager.getCache(CacheKey.KEY_FINANCE).evict(companyName);
    }
}

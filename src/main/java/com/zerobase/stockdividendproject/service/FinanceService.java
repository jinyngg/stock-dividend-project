package com.zerobase.stockdividendproject.service;

import com.zerobase.stockdividendproject.Repository.CompanyRepository;
import com.zerobase.stockdividendproject.Repository.DividendRepository;
import com.zerobase.stockdividendproject.Type.CacheKey;
import com.zerobase.stockdividendproject.entity.Company;
import com.zerobase.stockdividendproject.entity.Dividend;
import com.zerobase.stockdividendproject.exception.StockDividendException;
import com.zerobase.stockdividendproject.model.CompanyDto;
import com.zerobase.stockdividendproject.model.DividendDto;
import com.zerobase.stockdividendproject.model.ScrapedResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.zerobase.stockdividendproject.Type.ErrorCode.COMPANY_NOT_FOUND;

@Slf4j
@Service
@AllArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    // key =>
    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName) {
        
        // Redis에서 가져오는 데이터인지 확인하기 위해 로그 작성
        log.info("Search company -> " + companyName);
        
        // 회사명을 기준으로 회사 정보를 조회
        Company company = this.companyRepository.findByName(companyName)
                .orElseThrow(() -> new StockDividendException(COMPANY_NOT_FOUND));

        // 조회된 회사 ID로 배당금을 조회
        List<Dividend> dividendEntities = this.dividendRepository.findAllByCompanyId(company.getId());

        // 결과 조합 후 반환
        List<DividendDto> dividends = dividendEntities.stream()
                .map(dividend -> new DividendDto(dividend.getDate(), dividend.getDividend())
//                        Dividend.builder()
//                        .date(dividendEntity.getDate())
//                        .dividend(dividendEntity.getDividend())
//                        .build()
                        )
                .collect(Collectors.toList());

        return new ScrapedResult(new CompanyDto(company.getTicker(), company.getName())
//                Company.builder()
//                .ticker(company.getTicker())
//                .name(company.getName())
//                .build()
                , dividends
        );
    }
}

package com.zerobase.stockdividendproject.service;

import com.zerobase.stockdividendproject.model.Company;
import com.zerobase.stockdividendproject.model.Dividend;
import com.zerobase.stockdividendproject.model.ScrapedResult;
import com.zerobase.stockdividendproject.persist.CompanyRepository;
import com.zerobase.stockdividendproject.persist.DividendRepository;
import com.zerobase.stockdividendproject.persist.entity.CompanyEntity;
import com.zerobase.stockdividendproject.persist.entity.DividendEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public ScrapedResult getDividendByCompanyName(String companyName) {
        
        // 회사명을 기준으로 회사 정보를 조회
        CompanyEntity company = this.companyRepository.findByName(companyName)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회사명입니다."));

        // 조회된 회사 ID로 배당금을 조회
        List<DividendEntity> dividendEntities = this.dividendRepository.findAllByCompanyId(company.getId());

        // 결과 조합 후 반환
        List<Dividend> dividends = dividendEntities.stream()
                .map(dividendEntity -> Dividend.builder()
                        .date(dividendEntity.getDate())
                        .dividend(dividendEntity.getDividend())
                        .build())
                .collect(Collectors.toList());

        return new ScrapedResult(Company.builder()
                .ticker(company.getTicker())
                .name(company.getName())
                .build(), dividends
        );
    }
}

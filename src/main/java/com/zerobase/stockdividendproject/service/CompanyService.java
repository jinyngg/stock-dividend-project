package com.zerobase.stockdividendproject.service;

import com.zerobase.stockdividendproject.Repository.CompanyRepository;
import com.zerobase.stockdividendproject.Repository.DividendRepository;
import com.zerobase.stockdividendproject.entity.Company;
import com.zerobase.stockdividendproject.entity.Dividend;
import com.zerobase.stockdividendproject.exception.StockDividendException;
import com.zerobase.stockdividendproject.model.CompanyDto;
import com.zerobase.stockdividendproject.model.ScrapedResult;
import com.zerobase.stockdividendproject.scraper.Scraper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.zerobase.stockdividendproject.Type.ErrorCode.*;

@Slf4j
@Service
@AllArgsConstructor
public class CompanyService {

    private final Trie trie;

    private final Scraper yahooFinenceScraper;
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public CompanyDto save(String ticker) {
        // 회사 여부를 boolean 값으로 받아온다.
        boolean exists = this.companyRepository.existsByTicker(ticker);
        if (exists) {
//            throw new RuntimeException("already exists ticker -> " + ticker);

            log.error("already exists ticker -> " + ticker);
            throw new StockDividendException(TICKER_ALREADY_EXISTS);
        }

        return this.storeCompanyAndDividend(ticker);
    }

    public Page<Company> getAllCompany(Pageable pageable) {
        return this.companyRepository.findAll(pageable);
    }

    private CompanyDto storeCompanyAndDividend(String ticker) {

        // ticker를 기준으로 회사를 스크래핑
        CompanyDto companyDto = this.yahooFinenceScraper.scrapCompanyByTicker(ticker);
        if (ObjectUtils.isEmpty(companyDto)) {
//            throw new RuntimeException("failed to scrap ticker -> " + ticker);

            log.error("failed to scrap ticker -> " + ticker);
            throw new StockDividendException(SCRAP_TICKER_NOT_FOUND);
        }

        // 해당 회사가 존재할 경우, 회사의 배당금 정보를 스크래핑
        ScrapedResult scrapedResult = this.yahooFinenceScraper.scrap(companyDto);

        // 스크래핑 결과
        Company company = this.companyRepository.save(new Company(companyDto));
        List<Dividend> dividendEntities = scrapedResult.getDividends().stream()
                .map(dividend -> new Dividend(company.getId(), dividend))
                .collect(Collectors.toList());

        this.dividendRepository.saveAll(dividendEntities);
        return companyDto;
    }

    public List<String> getCompanyNameByKeyword(String keyword) {
        Pageable limit = PageRequest.of(0, 10);
        Page<Company> companyEntities = this.companyRepository.findByNameStartingWithIgnoreCase(keyword, limit);
        return companyEntities.stream()
                .map(companyEntity -> companyEntity.getName())
                .collect(Collectors.toList());
    }

    public void addAutocompleteKeyword(String keyword){
        // trie는 key, value를 같이 저장할 수 있으나 자동완성 기능만 구현하기 위해 value는 null 입력
        this.trie.put(keyword, null);
    }

    public List<String> autocomplete(String Keyword) {
        return (List<String>) this.trie.prefixMap(Keyword).keySet()
                .stream()
//                .limit(10)
                .collect(Collectors.toList());
    }

    public void deleteAutocompleteKeyword(String keyword) {
        this.trie.remove(keyword);
    }

    public String deleteCompany(String ticker) {

        // 회사 정보 조회
        var company = this.companyRepository.findByTicker(ticker)
                .orElseThrow(() -> new StockDividendException(TICKER_NOT_FOUND));

        // 회사 정보 및 배당금 정보 삭제
        this.dividendRepository.findAllByCompanyId(company.getId());
        this.companyRepository.delete(company);
        
        // 자동완성 정보(Trie) 제거
        this.deleteAutocompleteKeyword(company.getName());
        return company.getName();
    }
}

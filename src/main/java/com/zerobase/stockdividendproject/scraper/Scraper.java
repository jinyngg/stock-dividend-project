package com.zerobase.stockdividendproject.scraper;

import com.zerobase.stockdividendproject.model.CompanyDto;
import com.zerobase.stockdividendproject.model.ScrapedResult;

public interface Scraper {
    CompanyDto scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(CompanyDto companyDto);
}

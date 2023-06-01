package com.zerobase.stockdividendproject.scraper;

import com.zerobase.stockdividendproject.model.Company;
import com.zerobase.stockdividendproject.model.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}

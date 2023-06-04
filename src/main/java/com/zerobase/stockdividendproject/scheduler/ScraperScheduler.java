package com.zerobase.stockdividendproject.scheduler;

import com.zerobase.stockdividendproject.model.Company;
import com.zerobase.stockdividendproject.model.ScrapedResult;
import com.zerobase.stockdividendproject.persist.CompanyRepository;
import com.zerobase.stockdividendproject.persist.DividendRepository;
import com.zerobase.stockdividendproject.persist.entity.CompanyEntity;
import com.zerobase.stockdividendproject.persist.entity.DividendEntity;
import com.zerobase.stockdividendproject.scraper.YahooFinanceScraper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ScraperScheduler {

    private final CompanyRepository companyRepository;
    private final YahooFinanceScraper yahooFinanceScraper;
    private final DividendRepository dividendRepository;

    // cron => config 설정으로 관리하는게 좋다. 서버 배포를 다시하지 않아도 설정값만 바꿔서 재실행해주면 된다.
//    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling() {

        log.info("Scraping scheduler is started");
        
        // 저장된 회사 목록을 조회
        List<CompanyEntity> companies = this.companyRepository.findAll();

        // 회사마다 배당금 정보를 새로 스크래핑
        for (var company : companies) {
            log.info("company -> " + company.getName());
            ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(
                    new Company((company.getTicker()), (company.getName()))
//                    Company.builder()
//                            .name(company.getName())
//                            .ticker(company.getTicker())
//                            .build()
            );

            // 스크래핑만 대방금 정보 중 데이터베이스에 없는 값을 저장
            scrapedResult.getDividends().stream()
                    // 디비든 모델은 디비든 엔티티로 매핑
                    .map(dividend -> new DividendEntity(company.getId(), dividend))
                    // 엘리멘트를 하나씩 디비든 레파지토리에 삽입
                    .forEach(dividendEntity -> {
                        boolean exists = dividendRepository.existsByCompanyIdAndDate(dividendEntity.getCompanyId(), dividendEntity.getDate());
                        if (!exists) {
                            this.dividendRepository.save(dividendEntity);
                        }
                    });

            // 연속적으로 스크래핑 대상 사이트 서버에 요청을 날리지 않도록 일시정지
            try {
                Thread.sleep(3000); // 3초 일시정지
                
                // InterruptedException => 인터럽트를 받는 스레드가 blocking 될 수 있는 메소드를 실행할 때 발생
            } catch (InterruptedException e) {
                e.printStackTrace();
                // 쓰레드 관련 공부
                Thread.currentThread().interrupt();
            }

            // sleep() vs wait()
            // sleep() => 일정 시간이 지나면 작업이 스스로 진행.
            // wait() => 스레드를 대기 상태로 빠뜨림 => notify(), notifyAll() 메소드를 호출할 때까지 자동으로 깨지 않음.

        }



    } 
}

package com.zerobase.stockdividendproject.scraper;

import com.zerobase.stockdividendproject.model.Company;
import com.zerobase.stockdividendproject.model.Dividend;
import com.zerobase.stockdividendproject.model.ScrapedResult;
import com.zerobase.stockdividendproject.model.constants.Month;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class YahooFinanceScraper {

    // TODO 메모리, 가비지 컬렉션(GC) 개념을 공부하자!
    // 메모리 관점에서 맴버변수로 분리하는게 이점이 있다.
    // 클래스의 맴버 변수로 저장된 값은 Heap 메모리에 저장, 클래스의 내부에서 정리된 변수는 Stack 공간에 저장
    // Stack 공간에 저장된 변수는 함수가 호출될 때마다 생성, 함수가 제거될 때 제거, 같은 메소드를 동시에 호출하면 각각 다른 영역을 할당
    // 추가적으로 동시에 서로간의 데이터는 공유되지 않으므로 메모리 관점에서 불리함.

    // 가비지 콜랙션 =>
    // 메모리 할당 후 해제가 안되면 메모리 누수 발생
    // 수동으로 개발자가 직접 해제할 경우, 올바르게 해제되지 않아 버그 발생 가능성이 높다.
    // 가비지 컬렉터는 동적으로 할당된 메모리 영역 중 더 이상 쓰이지 않는 영역을 찾아내서 해제.
    // 가비지 컬렉터가 자주 실행되는게 좋은 지표는 절대 아니다.

    // static =>
    // 클래스로 인스턴스를 생성할 때, 여러 인스턴스를 생성하여 사용할 수도 있다. 그렇게 되면 각 인스턴스들은 Heap 내부의 각각의 메모리로 생성
    // static으로 선언될 경우 static 영역의 저장, 각각 다른 인스턴스로 생성되더라도 static 영역의 한 값을 가리키게 된다.
    // static은 잘 이해하고 작성되어야 한다.(중요)

    // 상수는 관례적으로 대문자로 작성
    private static final String STATICS_URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";
    private static final long START_TIME = 86400; // 60 * 60 * 24

    public ScrapedResult scrap(Company company) {

        var scarpResult = new ScrapedResult();
        scarpResult.setCompany(company);

        try {
            long now = System.currentTimeMillis() / 1000;

            String url = String.format(STATICS_URL, company.getTicker(), START_TIME, now);

            Connection connect = Jsoup.connect(url);
            Document document = connect.get();

            Elements parsingDivs = document.getElementsByAttributeValue("data-test", "historical-prices");
            Element tableElement = parsingDivs.get(0);

			/*
			 thead => element.children().get(0)
			 tbody => element.children().get(1)
			 tfoot => element.children().get(2)
			 */
            Element tbody = tableElement.children().get(1);

            List<Dividend> dividends = new ArrayList<>();
            for (Element e : tbody.children()) {
                String txt = e.text();

                // 배당금은 "0.25 Dividend" 형식으로 출력
                if (!txt.endsWith("Dividend")) {
                    continue;
                }

                String[] splits = txt.split(" ");
                int year = Integer.parseInt(splits[2]);
                int month = Month.strToNumber(splits[0]);
                int day = Integer.parseInt(splits[1].replace(",", ""));
                String dividend = splits[3];

                if (month < 0) {
                    throw new RuntimeException("Unexpected Month enum value -> " + splits[0]);
                }

                dividends.add(Dividend.builder()
                        .date(LocalDateTime.of(year, month, day, 0, 0))
                        .dividend(dividend)
                        .build());

//                System.out.println(year + "/" + month + "/" + day + "->" + dividend);
            }

            scarpResult.setDividendEntities(dividends);

        } catch (Exception e) {
            // TODO
            e.printStackTrace();
        }

        return scarpResult;
    }

    public Company scarpCompanyByTicker(String ticker) {
        return null;
    }
}

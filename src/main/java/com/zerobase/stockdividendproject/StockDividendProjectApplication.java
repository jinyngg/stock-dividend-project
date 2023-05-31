package com.zerobase.stockdividendproject;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockDividendProjectApplication {

	public static void main(String[] args) {
//		SpringApplication.run(StockDividendProjectApplication.class, args);

		try {

			Connection connect = Jsoup.connect("https://finance.yahoo.com/quote/COKE/history?period1=99100800&period2=1685491200&interval=1mo&filter=history&frequency=1mo&includeAdjustedClose=true");
			Document document = connect.get();

			Elements elements = document.getElementsByAttributeValue("data-test", "historical-prices");
			Element element = elements.get(0);

			/*
			 thead => element.children().get(0)
			 tbody => element.children().get(1)
			 tfoot => element.children().get(2)
			 */
			Element tbody = element.children().get(1);
			for (Element e : tbody.children()) {
				String txt = e.text();

				// 배당금은 "0.25 Dividend" 형식으로 출력
				if (!txt.endsWith("Dividend")) {
					continue;
				}

				String[] splits = txt.split(" ");
				String month = splits[0];
				int day = Integer.parseInt(splits[1].replace(",", ""));
				int year = Integer.parseInt(splits[2]);
				String dividend = splits[3];

				System.out.println(year + "/" + month + "/" + day + "->" + dividend);

			}


		} catch (Exception e) {

		}
	}

}

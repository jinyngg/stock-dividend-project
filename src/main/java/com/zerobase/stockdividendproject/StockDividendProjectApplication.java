package com.zerobase.stockdividendproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StockDividendProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockDividendProjectApplication.class, args);
	}
}

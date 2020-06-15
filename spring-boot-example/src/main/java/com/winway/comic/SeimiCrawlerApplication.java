package com.winway.comic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ComponentScan(basePackages = {"com.winway.comic"})
public class SeimiCrawlerApplication {
	public static void main(String[] args) {
		SpringApplication.run(SeimiCrawlerApplication.class, args);
	}
}

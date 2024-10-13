package com.trungnguyen.borrowbookservice;

import com.trungnguyen.borrowbookservice.config.AxonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableDiscoveryClient
@Import({AxonConfig.class})
public class BorrowbookserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BorrowbookserviceApplication.class, args);
	}

}

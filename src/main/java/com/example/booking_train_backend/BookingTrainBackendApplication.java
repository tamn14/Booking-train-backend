package com.example.booking_train_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BookingTrainBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingTrainBackendApplication.class, args);
	}

}

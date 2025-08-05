package com.example.booking_train_backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BookingTrainBackendApplication {

	public static void main(String[] args) {
		try {
			Dotenv dotenv = Dotenv.load();
			dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		} catch (Exception e) {
			// Xử lý lỗi nếu file .env không tồn tại, có thể bỏ qua nếu đang chạy trên server
			System.err.println("Could not load .env file. Falling back to system environment variables.");
		}
		SpringApplication.run(BookingTrainBackendApplication.class, args);
	}

}

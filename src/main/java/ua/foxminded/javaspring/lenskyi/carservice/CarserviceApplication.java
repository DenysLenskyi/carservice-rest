package ua.foxminded.javaspring.lenskyi.carservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ua.foxminded.javaspring.lenskyi.carservice.util.DataLoader;

@SpringBootApplication
public class CarserviceApplication {

	private static DataLoader dataLoader;

	public CarserviceApplication() {
	}

	public static void main(String[] args) {
		SpringApplication.run(CarserviceApplication.class, args);
	}

}
package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		// http://127.0.0.1:8080/h2-console == h2-console로 접속
		// http://127.0.0.1:8080/swagger-ui/index.html == swagger로 접속
	}
}

package co.edu.uniquindio.biblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PrestamoApp {

    public static void main(String[] args) {
        SpringApplication.run(PrestamoApp.class, args);
    }

}

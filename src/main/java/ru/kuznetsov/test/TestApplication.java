package ru.kuznetsov.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import ru.kuznetsov.test.entities.Customer;
import ru.kuznetsov.test.repositories.CustomerRepository;

import java.time.LocalDate;


@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(CustomerRepository repository) {
        return (args) -> {
            repository.save(new Customer("Никита", "Мужской", LocalDate.of(1982, 11, 30)));
            repository.save(new Customer("Алина", "Женский", LocalDate.of(1956, 12, 1)));
            repository.save(new Customer("Евгений", "Мужской", LocalDate.of(2001, 1, 1)));
            repository.save(new Customer("Николай", "Мужской", LocalDate.of(2000, 9, 19)));

            Customer customer = repository.findById(1L).get();
        };
    }
}

package ru.kuznetsov.test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kuznetsov.test.entities.Customer;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByNameStartsWithIgnoreCase(String name);
}

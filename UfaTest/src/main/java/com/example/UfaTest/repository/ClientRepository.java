package com.example.UfaTest.repository;

import com.example.UfaTest.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    // Точное совпадение по ФИО
    Optional<Client> findByNameAndSurnameAndPatronymic(String name, String surname, String patronymic);
}

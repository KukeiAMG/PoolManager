package com.example.UfaTest.repository;

import com.example.UfaTest.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,Long> {
}

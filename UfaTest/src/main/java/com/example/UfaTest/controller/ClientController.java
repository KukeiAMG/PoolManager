package com.example.UfaTest.controller;


import com.example.UfaTest.DTO.ClientDTO;
import com.example.UfaTest.model.Client;
import com.example.UfaTest.service.ClientService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v0/pool")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    //получить клиента по ID
    @GetMapping("/client/get/{id}")
    public ResponseEntity<?> getClient(@PathVariable("id") Long id) {
        try {
            Client client = clientService.getClientById(id);
            return ResponseEntity.ok(client);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //получить всех клиентов
    @GetMapping("/client/all")
    public ResponseEntity<?> getAllClients() {
        try {
            List<ClientDTO> clients = clientService.getAllClients();
            if (clients.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(clients);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Ошибка доступа к данным");
        }
    }

    //добавить нового пользователя
    @PostMapping("/client/add")
    public ResponseEntity<?> addClient(@RequestBody @Valid Client client) {
        try {
            Client savedClient = clientService.addClient(client);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Клиент с таким email или телефоном уже существует");
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest()
                    .body("Некорректные данные клиента: " + e.getMessage());
        }
    }

    // обновление клиента
    @PostMapping("/client/update")
    public ResponseEntity<?> updateClient(@RequestBody @Valid Map<String, String> clientInfo) {
        try {
            if (!clientInfo.containsKey("id")) {
                throw new IllegalArgumentException("ID клиента обязателен для обновления");
            }
            Client updatedClient = clientService.updateClient(clientInfo);
            return ResponseEntity.ok(updatedClient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
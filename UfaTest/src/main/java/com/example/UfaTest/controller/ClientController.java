package com.example.UfaTest.controller;


import com.example.UfaTest.DTO.ClientDTO;
import com.example.UfaTest.model.Client;
import com.example.UfaTest.service.ClientService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Client> getClient(@PathVariable("id") Long id){
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    //получить всех клиентов
    @GetMapping("/client/all")
    public List<ClientDTO> getClients(){
        return clientService.getAllClients();
    }

    //добавить нового пользователя
    @PostMapping("/client/add")
    public ResponseEntity<Client> addClient(@RequestBody Client client){
        //TODO
        // добавить логирование
        System.out.println("Received client: " + client);

        return ResponseEntity.ok(clientService.addClient(client));
    }

    @PostMapping("/client/update")
    public ResponseEntity<Client> updateClient(@RequestBody @Valid Map<String, String> clientInfo){
        // проверка на наличие id
        if(!clientInfo.containsKey("id")){
            throw new RuntimeException("ID клиента обязателен для обновления");
        }

        return ResponseEntity.ok(clientService.updateClient(clientInfo));
    }

    @PostMapping("/timetable/reserve/{date}")
    public String reserve(@PathVariable("date") String date, @RequestBody Map<String, String> reserveInfo){

        if(!reserveInfo.containsKey("clientId")){
            throw new RuntimeException("ID клиента обязателен для записи");
        }

        Long clientId = Long.parseLong(reserveInfo.get("clientId"));
        String datetime = reserveInfo.get("datetime");

        return clientService.reserve(clientId, datetime, date);
    }
}
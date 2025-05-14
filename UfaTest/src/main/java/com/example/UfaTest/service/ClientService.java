package com.example.UfaTest.service;

import com.example.UfaTest.DTO.ClientDTO;
import com.example.UfaTest.model.Client;
import com.example.UfaTest.model.Reservation;
import com.example.UfaTest.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    @Transactional(readOnly = true)
    public Client getClientById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Некорректный ID клиента");
        }

        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Клиент с ID " + id + " не найден"));
    }

    @Transactional(readOnly = true)  // Добавлено readOnly для оптимизации
    public List<ClientDTO> getAllClients() {
        try {
            List<Client> clientList = clientRepository.findAll();
            List<ClientDTO> clientDTOList = new ArrayList<>(clientList.size());  // Оптимизация размера

            for (Client client : clientList) {
                if (client != null) {  // Защита от null-значений
                    clientDTOList.add(new ClientDTO(client.getId(), client.getName()));
                }
            }
            return clientDTOList;
        } catch (DataAccessException e) {
            throw new DataRetrievalFailureException("Ошибка при получении списка клиентов", e);
        }
    }

    @Transactional
    public Client addClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Данные клиента не могут быть null");
        }
        // Дополнительные проверки перед сохранением
        if (client.getEmail() == null || client.getEmail().isBlank()) {
            throw new ConstraintViolationException("Email обязателен для заполнения", null);
        }
        if (client.getPhoneNumber() == null || client.getPhoneNumber().isBlank()) {
            throw new ConstraintViolationException("Номер телефона обязателен", null);
        }
        try {
            return clientRepository.save(client);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Ошибка сохранения клиента: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Client updateClient(Map<String, String> clientInfo) {

        // Проверка на null и пустоту
        if (clientInfo == null || clientInfo.isEmpty()) {
            throw new IllegalArgumentException("Информация клиента не может быть null или пустым");
        }

        // Проверка наличия обязательного поля id
        if (!clientInfo.containsKey("id")) {
            throw new IllegalArgumentException("id клиента обязателен для обновления");
        }

        Long id;
        try {
            id = Long.parseLong(clientInfo.get("id"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("id клиента должен быть действительным номером");
        }

        Client client = clientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Клиент с таким id не найден"));
        // поочередно берет пару ключ значение из clientInfo и складывает в entry
        for (Map.Entry<String, String> entry : clientInfo.entrySet()) {

            // складываем значение ключа и значения в переменные
            String key = entry.getKey();
            String value = entry.getValue();

            System.out.println(key);
            System.out.println(value);

            // проверяем какое значение ключа и в зависимости от него мы понимаем какое поле менять, а потом устанавливаем его значение
            switch (key) {
                case "name":
                    client.setName(value);
                    break;
                case "surname":
                    client.setSurname(value);
                    break;
                case "patronymic":
                    client.setPatronymic(value);
                    break;
                case "phoneNumber":
                    client.setPhoneNumber(value);
                    break;
                case "email":
                    client.setEmail(value);
                    break;
            }
        }

        try {
            return clientRepository.save(client);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Ошибка целостности данных", e);
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Нет такого пользователя");
        }
    }

    @Transactional(readOnly = true)
    public Client getClientByFullname(String name, String surname, String patronymic) {
        if (name == null || name.isBlank() || surname == null || surname.isBlank() || patronymic == null || patronymic.isBlank())
            throw new IllegalArgumentException("Имя, фамилия и отчество не могут быть пустыми");

        return clientRepository.findByNameAndSurnameAndPatronymic(name, surname, patronymic).orElseThrow(()
                -> new EntityNotFoundException("Клиент с таким именем, фамилией и отчеством не найден: " + name + surname + patronymic));
    }
}

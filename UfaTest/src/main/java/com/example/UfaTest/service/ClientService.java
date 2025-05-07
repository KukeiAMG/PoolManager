package com.example.UfaTest.service;

import com.example.UfaTest.DTO.ClientDTO;
import com.example.UfaTest.model.Client;
import com.example.UfaTest.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    public Client getClientById(Long id){
        return clientRepository.findById(id).orElseThrow(()->new RuntimeException("Клиент с таким id не найден"));
    }

    public List<ClientDTO> getAllClients(){
        List <Client> clientList = clientRepository.findAll();
        List <ClientDTO> clientDTOList = new ArrayList<>();

         for(Client client : clientList){
             clientDTOList.add(new ClientDTO(client.getId(), client.getName()));
         }
        return clientDTOList;
    }

    public Client addClient(Client client){
        return clientRepository.save(client);
    }

    public Client updateClient(Map<String, String> clientInfo){
        Long id = Long.parseLong(clientInfo.get("id"));

        Client client = clientRepository.findById(id).orElseThrow(()->new RuntimeException("Клиент с таким id не найден"));

        // поочередно берет пару ключ значение из clientInfo и складывает в entry
        for(Map.Entry<String, String> entry : clientInfo.entrySet()){

            // складываем значение ключа и значения в переменные
            String key = entry.getKey();
            String value = entry.getValue();

            System.out.println(key);
            System.out.println(value);

            // проверяем какое значение ключа и в зависимости от него мы понимаем какое поле менять, а потом устанавливаем его значение
            switch(key){
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

        System.out.println(client);
        return clientRepository.save(client);
    }


    public String reserve(Long id, String datetime, String time){
        // TODO
        //  проверить на наличие такого клиента
        //  а


        return "hello";
    }


}

package com.areus.client.backend.service;

import com.areus.client.backend.jpa.entity.Client;
import com.areus.client.backend.jpa.repository.ClientRepository;
import com.areus.client.backend.exception.ClientNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class ClientService {
    ClientRepository clientRepository;

    /**
     * @return The list of the clients
     */
    public List<Client> getAllClients(){
        log.info("getAllClients()");
        return clientRepository.findAll();
    }

    /**
     * @param id The id of the client
     * @return The client with the given id
     */
    public Client getClient(Long id) throws ClientNotFoundException {
        log.info("getClient({})", id);
        return clientRepository.findById(id)
                .orElseThrow(
                        () -> new ClientNotFoundException(String.format("Client not found with ID: %d", id))
        );
    }

    /**
     * @return The list of the clients aged between 18 and 40
     */
    public List<Client> getClientBetween18and40() {
        log.info("getClientBetween18and40()");
        return clientRepository.findAll().stream().filter(client -> {
            LocalDate birthdate = client.getDateOfBirth();

            // Clients aged between 18 and 40: Return true if the client is exactly 18 or if older than 18 but younger than 41
            return birthdate.isEqual(LocalDate.now().minusYears(18)) || (birthdate.isBefore(LocalDate.now().minusYears(18)) &&
                    birthdate.isAfter(LocalDate.now().minusYears(41)));
        }).collect(Collectors.toList());
    }

    /**
     * @return The average age of clients
     */
    public Integer getAverageClientAge() {
        log.info("getAverageClientAge()");
        return clientRepository.getAverageClientAge();
    }

    /**
     * Inserts the received client object in to the database.
     * @param client The client to be saved
     * @return The saved client
     */
    public Client saveClient(Client client) {
        log.info("createClient({})", client);
        return clientRepository.save(client);
    }

    /**
     * Updates the client object with the given ID if it is found in the database.
     * @param updateClient The client object containing the changes of the client with the given id.
     * @param id The ID of the client to be updated.
     * @throws ClientNotFoundException Throws exception if client not found.
     */
    public Client updateClient(Client updateClient, Long id) throws ClientNotFoundException {
        log.info("updateClient({}, {})", updateClient, id);
        return clientRepository.findById(id)
                .map(client -> {
                    // Mapping can become particularly long for entities with many fields. Implementing a mapper solution can improve this.
                    client.setName(updateClient.getName());
                    client.setAddress(updateClient.getAddress());
                    client.setPhoneNumber(updateClient.getPhoneNumber());
                    client.setEmail(updateClient.getEmail());
                    client.setDateOfBirth(updateClient.getDateOfBirth());
                    client.setIdentificationNumber(updateClient.getIdentificationNumber());
                    client.setAccountNumber(updateClient.getAccountNumber());
                    client.setAccountType(updateClient.getAccountType());
                    client.setAccountType(updateClient.getAccountType());
                    client.setRiskProfile(updateClient.getRiskProfile());
                    return clientRepository.save(client);
                }).orElseThrow(
                        () -> new ClientNotFoundException(String.format("Client not found with ID: %d", id))
        );
    }

    /**
     * Deletes the client with the given id.
     * @param id The ID of the client.
     */
    public void deleteClient(Long id) {
        log.info("deleteClient({})", id);
        clientRepository.deleteById(id);
    }
}

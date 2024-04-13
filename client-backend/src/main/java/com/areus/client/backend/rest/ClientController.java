package com.areus.client.backend.rest;

import com.areus.client.backend.jpa.entity.Client;
import com.areus.client.backend.service.ClientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

// TODO add exception handling for methods
// TODO update javadoc with exception handling

@AllArgsConstructor
@RestController
public class ClientController {

    ClientService clientService;

    /**
     * @return The list of the clients
     */
    @GetMapping("/client/all")
    ResponseEntity<List<Client>> getAllClients() {
        return new ResponseEntity<>(clientService.getAllClients(), HttpStatus.OK);
    }

    /**
     * @param id The id of the client
     * @return The client with the given id
     */
    @GetMapping("/client/{id}")
    ResponseEntity<Client> getClient(@PathVariable Long id) {
        return new ResponseEntity<>(clientService.getClient(id), HttpStatus.OK);
    }

    /**
     * @return The clients aged between 18 and 40.
     */
    @GetMapping("/client/age/between-18-and-40")
    ResponseEntity<List<Client>> getClientsBetween18and40(){
        return new ResponseEntity<>(clientService.getClientBetween18and40(), HttpStatus.OK);
    }

    /**
     * @return The average age of the clients.
     */
    @GetMapping("/client/age/average")
    ResponseEntity<Integer> getAverageClientAge() {
        return new ResponseEntity<>(clientService.getAverageClientAge(), HttpStatus.OK);
    }

    /**
     * Creates the client in the database.
     * @param client The client to be saved in the database.
     * @return The client that was saved.
     */
    @PostMapping("/client")
    ResponseEntity<Client> createClient(@Valid @RequestBody Client client) {
        return new ResponseEntity<>(clientService.createClient(client), HttpStatus.CREATED);
    }

    /**
     * Updates the details of the client with the specified ID if it exists.
     * The reason patch (update) is used instead of the usual put (replace) is because auto-incremented sequence is used to generate the IDs.
     * If a put request comes in with an ID that does not yet have a client, but the sequence will soon step on it, it will lead to a conflict.
     *
     * @param client The client who needs to be updated
     * @param id The id of the client
     * @return On a successful request, returns the NO_CONTENT HTTP status code.
     */
    @PatchMapping("/client/{id}")
    ResponseEntity<Client> updateClient(@Valid @RequestBody Client client, @PathVariable Long id) {
        clientService.updateClient(client, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Deletes the client with the specified ID if it exists.
     * @param id The ID of the client.
     * @return On a successful request, returns the NO_CONTENT HTTP status code.
     */
    @DeleteMapping("/client/{id}")
    ResponseEntity<Client> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
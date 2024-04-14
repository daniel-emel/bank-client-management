package com.areus.client.backend;

import com.areus.client.backend.jpa.entity.Client;
import com.areus.client.backend.jpa.repository.ClientRepository;
import com.areus.client.backend.service.ClientService;
import com.areus.client.backend.util.AccountStatus;
import com.areus.client.backend.util.AccountType;
import com.areus.client.backend.util.RiskProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * This class houses BDD-style unit tests designed to verify the proper functionality of the ClientService class.
 */
@SpringBootTest
public class ClientServiceUnitTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    Client client;

    @BeforeEach
    public void setup(){
        client = Client.builder()
                .id(1L)
                .name("Giovanni Rossi")
                .address("Via Roma 3, Rome, Italy")
                .phoneNumber("+391234567890")
                .email("giovanni.rossi@example.com")
                .dateOfBirth(LocalDate.of(1988,3,10))
                .identificationNumber("IT5678901234")
                .accountNumber("34567890-65432109-76543210")
                .accountType(AccountType.SAVINGS)
                .accountStatus(AccountStatus.ACTIVE)
                .riskProfile(RiskProfile.CONSERVATIVE)
                .build();
    }

    @DisplayName("JUnit test for saveClient() method")
    @Test
    public void givenClientObject_whenSaveClient_thenReturnClientObject(){
        // given - precondition or setup
        given(clientRepository.findById(client.getId())).willReturn(Optional.empty());

        given(clientRepository.save(client)).willReturn(client);

        // when -  action or the behaviour to test
        Client savedClient = clientService.saveClient(client);

        // then - verify the output
        assertThat(savedClient).isNotNull();
    }

    @DisplayName("JUnit test for getAllClients() method")
    @Test
    public void givenClientsList_whenGetAllClients_thenReturnClientsList(){
        // given - precondition or setup
        Client client2 =  Client.builder()
                .name("Maria García")
                .address("Calle Principal 4, Madrid, Spain")
                .phoneNumber("+34123456789")
                .email("maria.garcia@example.com")
                .dateOfBirth(LocalDate.of(1993,11,25))
                .identificationNumber("ES2345678901")
                .accountNumber("45678901-54321098-65432109")
                .accountType(AccountType.CHECKING)
                .accountStatus(AccountStatus.ACTIVE)
                .riskProfile(RiskProfile.MODERATELY_CONSERVATIVE)
                .build();

        given(clientRepository.findAll()).willReturn(List.of(client,client2));

        // when -  action or the behaviour to test
        List<Client> clientList = clientService.getAllClients();

        // then - verify the output
        assertThat(clientList).isNotNull();
        assertThat(clientList.size()).isEqualTo(2);
    }

    @DisplayName("JUnit test for getClient() method")
    @Test
    public void givenClientId_whenGetClient_thenReturnClientObject(){
        // given - precondition or setup
        given(clientRepository.findById(1L)).willReturn(Optional.of(client));

        // when -  action or the behaviour to test
        Client savedClient = clientService.getClient(client.getId());

        // then - verify the output
        assertThat(savedClient).isNotNull();

    }

    @DisplayName("JUnit test for updateClient() method")
    @Test
    public void givenClientObject_whenUpdateClient_thenReturnUpdatedClient(){
        // given - precondition or setup
        given(clientRepository.save(client)).willReturn(client);
        given(clientRepository.findById(client.getId())).willReturn(Optional.of(client));

        client.setName("Test Test");
        client.setEmail("test@example.com");

        // when -  action or the behaviour to test
        Client updatedClient = clientService.updateClient(client, client.getId());

        // then - verify the output
        assertThat(updatedClient.getName()).isEqualTo("Test Test");
        assertThat(updatedClient.getEmail()).isEqualTo("test@example.com");

    }

    @DisplayName("JUnit test for deleteClient() method")
    @Test
    public void givenClientId_whenDeleteClient_thenNothing(){
        // given - precondition or setup
        long clientId = 1L;
        willDoNothing().given(clientRepository).deleteById(clientId);

        // when -  action or the behaviour to test
        clientService.deleteClient(clientId);

        // then - verify the output
        verify(clientRepository, times(1)).deleteById(clientId);
    }

}

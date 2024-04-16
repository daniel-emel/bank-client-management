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

    Client client1, client2, client17YearsOld, client18YearsOld, client30YearsOld, client40YearsOld, client41YearsOld;

    @BeforeEach
    public void setup(){
        client1 = Client.builder()
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

        client2 =  Client.builder()
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

        client17YearsOld = Client.builder()
                .name("Maria García")
                .address("Calle Principal 4, Madrid, Spain")
                .phoneNumber("+34123456789")
                .email("maria.garcia@example.com")
                .dateOfBirth(LocalDate.of(2007,4,1))
                .identificationNumber("ES2345678901")
                .accountNumber("45678901-54321098-65432109")
                .accountType(AccountType.CHECKING)
                .accountStatus(AccountStatus.ACTIVE)
                .riskProfile(RiskProfile.MODERATELY_CONSERVATIVE)
                .build();

        client18YearsOld =  Client.builder()
                .name("Maria García")
                .address("Calle Principal 4, Madrid, Spain")
                .phoneNumber("+34123456789")
                .email("maria.garcia@example.com")
                .dateOfBirth(LocalDate.of(2006,4,1))
                .identificationNumber("ES2345678901")
                .accountNumber("45678901-54321098-65432109")
                .accountType(AccountType.CHECKING)
                .accountStatus(AccountStatus.ACTIVE)
                .riskProfile(RiskProfile.MODERATELY_CONSERVATIVE)
                .build();

        client30YearsOld =  Client.builder()
                .name("Maria García")
                .address("Calle Principal 4, Madrid, Spain")
                .phoneNumber("+34123456789")
                .email("maria.garcia@example.com")
                .dateOfBirth(LocalDate.of(1994,4,1))
                .identificationNumber("ES2345678901")
                .accountNumber("45678901-54321098-65432109")
                .accountType(AccountType.CHECKING)
                .accountStatus(AccountStatus.ACTIVE)
                .riskProfile(RiskProfile.MODERATELY_CONSERVATIVE)
                .build();

        client40YearsOld = Client.builder()
                .name("Maria García")
                .address("Calle Principal 4, Madrid, Spain")
                .phoneNumber("+34123456789")
                .email("maria.garcia@example.com")
                .dateOfBirth(LocalDate.of(1984,4,1))
                .identificationNumber("ES2345678901")
                .accountNumber("45678901-54321098-65432109")
                .accountType(AccountType.CHECKING)
                .accountStatus(AccountStatus.ACTIVE)
                .riskProfile(RiskProfile.MODERATELY_CONSERVATIVE)
                .build();

        client41YearsOld = Client.builder()
                .name("Maria García")
                .address("Calle Principal 4, Madrid, Spain")
                .phoneNumber("+34123456789")
                .email("maria.garcia@example.com")
                .dateOfBirth(LocalDate.of(1983,4,1))
                .identificationNumber("ES2345678901")
                .accountNumber("45678901-54321098-65432109")
                .accountType(AccountType.CHECKING)
                .accountStatus(AccountStatus.ACTIVE)
                .riskProfile(RiskProfile.MODERATELY_CONSERVATIVE)
                .build();
    }

    @DisplayName("JUnit test for getAllClients() method")
    @Test
    public void givenClientsList_whenGetAllClients_thenReturnClientsList(){
        // given - precondition or setup
        given(clientRepository.findAll()).willReturn(List.of(client1,client2));

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
        given(clientRepository.findById(1L)).willReturn(Optional.of(client1));

        // when -  action or the behaviour to test
        Client savedClient = clientService.getClient(client1.getId());

        // then - verify the output
        assertThat(savedClient).isNotNull();

    }

    @DisplayName("JUnit test for getClientBetween18and40() method")
    @Test
    public void givenNothing_getClientsBetween18and40_thenReturnClientsBetween18and40(){
        // given - precondition or setup
        given(clientRepository.findAll()).willReturn(List.of(client17YearsOld, client18YearsOld, client30YearsOld, client40YearsOld, client41YearsOld));

        // when -  action or the behaviour to test
        List<Client> clients = clientService.getClientBetween18and40();

        // then - verify the output
        assertThat(clients).isEqualTo(List.of(client18YearsOld, client30YearsOld, client40YearsOld));
    }

    @DisplayName("JUnit test for getAverageClientAge() method")
    @Test
    public void givenNothing_whenGetAverageClientAge_thenReturnAverageClientAge(){
        // given - precondition or setup
        given(clientRepository.getAverageClientAge()).willReturn(40);

        // when -  action or the behaviour to test
        Integer averageClientAge = clientService.getAverageClientAge();

        // then - verify the output
        assertThat(averageClientAge).isEqualTo(40);
    }

    @DisplayName("JUnit test for saveClient() method")
    @Test
    public void givenClientObject_whenSaveClient_thenReturnClientObject(){
        // given - precondition or setup
        given(clientRepository.findById(client1.getId())).willReturn(Optional.empty());

        given(clientRepository.save(client1)).willReturn(client1);

        // when -  action or the behaviour to test
        Client savedClient = clientService.saveClient(client1);

        // then - verify the output
        assertThat(savedClient).isNotNull();
    }

    @DisplayName("JUnit test for updateClient() method")
    @Test
    public void givenClientObject_whenUpdateClient_thenReturnUpdatedClient(){
        // given - precondition or setup
        given(clientRepository.save(client1)).willReturn(client1);
        given(clientRepository.findById(client1.getId())).willReturn(Optional.of(client1));

        client1.setName("Test Test");
        client1.setEmail("test@example.com");

        // when -  action or the behaviour to test
        Client updatedClient = clientService.updateClient(client1, client1.getId());

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

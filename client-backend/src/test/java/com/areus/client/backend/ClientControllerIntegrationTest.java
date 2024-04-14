package com.areus.client.backend;

import com.areus.client.backend.jpa.entity.Client;
import com.areus.client.backend.rest.ClientController;
import com.areus.client.backend.service.ClientService;
import com.areus.client.backend.util.AccountStatus;
import com.areus.client.backend.util.AccountType;
import com.areus.client.backend.util.RiskProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * The purpose of these tests is to check the correct functioning of the controller in the following aspects:
 *     - Listening to HTTP Requests, Deserializing Input, Validating Input,
 *     - Calling the Business Logic, Serializing the Output, Translating Exceptions
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    ClientController clientController;

    @Mock
    private ClientService clientService;

    private static ObjectMapper objectMapper = null;

    @Test
    public void testFindAllClient() {
        Client client1 = Client.builder()
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

        List<Client> clients = List.of(client1, client2);

        when(clientService.getAllClients()).thenReturn(clients);

        ResponseEntity<List<Client>> result = clientController.getAllClients();

        assertThat(Objects.requireNonNull(result.getBody()).size()).isEqualTo(2);
        assertThat(result.getBody().get(0).getName()).isEqualTo(client1.getName());
        assertThat(result.getBody().get(1).getName()).isEqualTo(client2.getName());
    }

    @Test
    public void testFindClient() throws Exception {
        Client client = Client.builder()
                .id(115L)
                .name("Andreas Schmidt")
                .address("Hauptplatz 5, Vienna, Austria")
                .phoneNumber("+431234567890")
                .email("andreas.schmidt@example.com")
                .dateOfBirth(LocalDate.of(1982,7,30))
                .identificationNumber("AT3456789012")
                .accountNumber("56789012-43210987-54321098")
                .accountType(AccountType.INVESTMENT)
                .accountStatus(AccountStatus.ACTIVE)
                .riskProfile(RiskProfile.AGGRESSIVE)
                .build();

        when(clientService.getClient(1L)).thenReturn(client);

        mockMvc.perform(MockMvcRequestBuilders.get("/client/115"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("115"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddValidClient() throws Exception {
        Client client = Client.builder()
                .id(10L)
                .name("Lars Jensen")
                .address("Hovedgade 11, Copenhagen, Denmark")
                .phoneNumber("+4512345678")
                .email("lars.jensen@example.com")
                .dateOfBirth(LocalDate.of(1987,10,5))
                .identificationNumber("DK1234567890")
                .accountNumber("67890123-32109876-43210987")
                .accountType(AccountType.SAVINGS)
                .accountStatus(AccountStatus.CLOSED)
                .riskProfile(RiskProfile.MODERATE)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/client")
                        .content(asJsonString(client))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddInvalidClient() throws Exception {
        Client client = Client.builder()
                .id(10L)
                .name("Elena Ivanova")
                .address("Ulitsa Lenina 12, Moscow, Russia")
                .phoneNumber("74951234567")
                .email("elena.ivanovaexample.com")
                .dateOfBirth(LocalDate.of(1991,4,15))
                .identificationNumber("RU0987654321")
                .accountNumber("78901234-21098765-32109876")
                .accountType(AccountType.CHECKING)
                .accountStatus(AccountStatus.ACTIVE)
                .riskProfile(RiskProfile.MODERATELY_AGGRESSIVE)
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/client")
                        .content(asJsonString(client))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber", Is.is("Invalid phone number.")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Is.is("Invalid email address.")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testPatchValidClient() throws Exception {
        Client client = Client.builder()
                .id(21L)
                .name("Marta Kowalska")
                .address("Aleje Jerozolimskie 13, Warsaw, Poland")
                .phoneNumber("+48123456789")
                .email("marta.kowalska@example.com")
                .dateOfBirth(LocalDate.of(1984,8,20))
                .identificationNumber("PL5678901234")
                .accountNumber("89012345-10987654-21098765")
                .accountType(AccountType.SAVINGS)
                .accountStatus(AccountStatus.ACTIVE)
                .riskProfile(RiskProfile.CONSERVATIVE)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/client/21")
                        .content(asJsonString(client))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(21))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testPatchInvalidClient() throws Exception {
        Client client = Client.builder()
                .id(100L)
                .name("Luca Bianchi")
                .address("Via Garibaldi 14, Milan, Italy")
                .phoneNumber("+391234567890")
                .email("luca.bianchi@example.com")
                .dateOfBirth(LocalDate.of(2025,4,15))
                .identificationNumber("IT2345678901")
                .accountNumber("3456-09876543-10987654")
                .accountType(AccountType.CHECKING)
                .accountStatus(AccountStatus.ACTIVE)
                .riskProfile(RiskProfile.MODERATELY_CONSERVATIVE)
                .build();
        mockMvc.perform(MockMvcRequestBuilders.patch("/client/37")
                        .content(asJsonString(client))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth", Is.is("Invalid date of birth.")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", Is.is("Invalid account number.")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testDeleteClient() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/client/100"))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.blankOrNullString()));
    }

    public static String asJsonString(final Object obj) {
        try {
            if (objectMapper == null) {
                objectMapper = new ObjectMapper();

                // Jackson (JSR-310): Register JavaTimeModule that enables mapping LocalDate objects to JSON strings.
                objectMapper.registerModule(new JavaTimeModule());
            }
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


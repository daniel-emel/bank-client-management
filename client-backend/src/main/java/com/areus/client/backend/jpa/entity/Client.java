package com.areus.client.backend.jpa.entity;

import com.areus.client.backend.util.AccountStatus;
import com.areus.client.backend.util.AccountType;
import com.areus.client.backend.util.RiskProfile;
import com.areus.client.backend.validation.DateOfBirth;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@ToString
@Builder @Validated
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "client", schema = "client")
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="client_seq")
    @SequenceGenerator(name = "client_seq", sequenceName = "client_seq", initialValue = 1, allocationSize=1)
    @Column(name = "id")
    private Long id;

    @Size(max = 100, message = "The name provided exceeds the maximum character limit.")
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 200, message = "The address provided exceeds the maximum character limit.")
    @Column(name = "address")
    private String address;

    @Pattern(regexp = "^\\+[0-9]{7,15}$")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Email
    @Column(name = "email", nullable = false)
    private String email;

    @DateOfBirth
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Pattern(regexp = "")
    @Column(name = "identification_number", nullable = false)
    private String identificationNumber;

    @Pattern(regexp = "^[0-9]{4}-[0-9]{4}-[0-9]{4}$")
    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false)
    private AccountStatus accountStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_profile", nullable = false)
    private RiskProfile riskProfile;
}

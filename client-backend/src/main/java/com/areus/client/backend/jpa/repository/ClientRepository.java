package com.areus.client.backend.jpa.repository;

import com.areus.client.backend.jpa.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClientRepository extends JpaRepository<Client,Long> {

    /**
     * The query below calculates the age of the clients in the subquery from the date of birth and then calculates their average.
     * Find detailed explanation here: <a href="https://www.scaler.com/topics/how-to-calculate-age-from-date-of-birth-in-sql/">How to Calculate Age From Date of Birth in SQL?</a>
     * @return Average age of clients
     */
    @Query("SELECT AVG(CAST(age as int)) FROM (SELECT DATE_FORMAT(FROM_DAYS(DATEDIFF(NOW(),c.dateOfBirth)), '%Y') AS age FROM Client c) AS client_age")
    Integer getAverageClientAge();
}

package com.example.cloud;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ContextConfiguration(classes = {CloudApplication.class, TestPostgresConfig.class})
class CloudApplicationTests {

   @Autowired
   private TestRestTemplate restTemplate;

   @Container
   private GenericContainer<?> cloudCont = new GenericContainer<>("cloud-cloud")
           .withExposedPorts(8080);

   @Container
   private PostgreSQLContainer<?> postgresDBCont = new PostgreSQLContainer<>("postgres:latest")
           .withDatabaseName("db")
           .withUsername("postgres")
           .withPassword("asdqwe")
           .withExposedPorts(5432)
           .waitingFor(Wait.forListeningPort());

   @Test
   void contextLoads() {
      int cloudPort = cloudCont.getMappedPort(8080);
      ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + cloudPort, String.class);
      System.out.println("\nCloud response: " + response.getBody());

      int postgresPort = postgresDBCont.getMappedPort(5432);
      System.out.println("Postgres DB is running on port: " + postgresPort);
   }

}
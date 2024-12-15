package com.example.cloud.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.cloud.domain.Authority;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

@DataJpaTest
@AllArgsConstructor
public class AuthorityRepositoryTests {

}
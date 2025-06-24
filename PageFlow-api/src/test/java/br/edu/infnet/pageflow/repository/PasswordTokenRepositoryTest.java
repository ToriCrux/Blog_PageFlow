package br.edu.infnet.pageflow.repository;

import br.edu.infnet.pageflow.entities.PasswordResetToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PasswordTokenRepositoryTest {

    @Autowired
    private PasswordTokenRepository repository;

    @Test
    void testSaveAndFind() {
    }
}


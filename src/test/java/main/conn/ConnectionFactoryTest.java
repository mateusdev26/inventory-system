package main.conn;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionFactoryTest {

    @Test
    @DisplayName("Should not threw a SQLException in getConnection()")
    void getConnection() {
        assertDoesNotThrow(ConnectionFactory::getConnection);
    }

}
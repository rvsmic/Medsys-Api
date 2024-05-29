package com.medsys.medsysapi.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QueryDispatcherTest {
    @InjectMocks
    QueryDispatcher queryDispatcher;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDispatch() throws QueryException {
        assertNotNull(queryDispatcher.dataSource);
        Connection c = null;
        try {
            c = queryDispatcher.dataSource.getConnection();
        } catch (SQLException e) {
            fail(e);
        }

        assertNotNull(c);

        try {
            assertTrue(c.isValid(0));
        } catch (SQLException e) {
            fail(e);
        }
    }

    @Test
    void testGetSecUserDetails() throws QueryException {

    }

    @Test
    void testGetIdUsername() throws QueryException {

    }

    @Test
    void testCheckPasswordValid() throws QueryException {
        String password = "test123";
        String hashedPassword = DigestUtils.md5DigestAsHex(password.getBytes()).toUpperCase();
        System.out.println(hashedPassword);
    }
}

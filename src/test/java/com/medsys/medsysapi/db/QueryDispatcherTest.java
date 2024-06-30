package com.medsys.medsysapi.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QueryDispatcherTest {
    @Autowired
    QueryDispatcher queryDispatcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDispatch() {
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
    void testGetSecUserDetails() {

    }

    @Test
    void testGetIdUsername() {

    }

    @Test
    void testCheckPasswordValid() {

    }
}

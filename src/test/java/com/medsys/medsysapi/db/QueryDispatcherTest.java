package com.medsys.medsysapi.db;

import com.medsys.medsysapi.security.SecUserDetails;
import com.medsys.medsysapi.security.SecUserDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class QueryDispatcherTest {
    @Mock
    SecUserDetailsService secUserDetailsService;
    @Autowired
    DataSource dataSource;
    @InjectMocks
    QueryDispatcher queryDispatcher;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        queryDispatcher.dataSource = dataSource;
        queryDispatcher.init();
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

    }
}

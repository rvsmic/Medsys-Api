package com.medsys.medsysapi.db;

import com.medsys.medsysapi.security.SecUserDetails;
import com.medsys.medsysapi.security.SecUserDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class QueryDispatcherTest {
    @Mock
    SecUserDetailsService secUserDetailsService;
    @InjectMocks
    QueryDispatcher queryDispatcher;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDispatch() throws QueryException {
        assertNotNull(jdbcTemplate);

        DriverManagerDataSource dataSource = (DriverManagerDataSource) jdbcTemplate.getDataSource();
        assertNotNull(dataSource);

        String table = "personnel";
        String column = "id";

        QueryResults queryResults = null;
        queryResults = QueryDispatcher.dispatch(jdbcTemplate, table, column);

        assertNotNull(queryResults);
    }

    @Test
    void testGetSecUserDetails() throws QueryException {
        JdbcTemplate mockTemplate = mock(JdbcTemplate.class);

        Map<String, Object> map = new HashMap<>();
        map.put("id", 0);
        map.put("name", "name");
        map.put("date_of_birth", new Date(0));
        map.put("pesel", null);
        map.put("gender", "a");
        map.put("phone_number", "phone_number");
        map.put("address", "address");
        map.put("speciality", null);
        map.put("username", "username");
        map.put("password", "password");
        map.put("profession", "profession");

        List<Map<String, Object>> mockList = List.of(map);
        when(mockTemplate.queryForList(anyString())).thenReturn(mockList);

        SecUserDetails secUserDetails = new SecUserDetails(map);
        SecUserDetails result = QueryDispatcher.getSecUserDetails(mockTemplate, anyInt());
        Assertions.assertEquals(secUserDetails, result);
    }

    @Test
    void testGetIdUsername() throws QueryException {
        JdbcTemplate mockTemplate = mock(JdbcTemplate.class);
        List<Map<String, Object>> mockList = List.of(Map.of("frederick", "610"));

        when(mockTemplate.queryForList(anyString())).thenReturn(mockList);

        int result = QueryDispatcher.getIdUsername(mockTemplate, "username");
        Assertions.assertEquals(610, result);
    }

    @Test
    void testCheckPasswordValid() throws QueryException {
        JdbcTemplate mockTemplate = mock(JdbcTemplate.class);
        List<Map<String, Object>> mockList = List.of(Map.of("frederick", "1"));

        when(mockTemplate.queryForList(anyString())).thenReturn(mockList);

        Boolean result = QueryDispatcher.checkPasswordValid(mockTemplate, 0, "password");
        Assertions.assertEquals(true, result);

        mockList = List.of(Map.of("frederick", "0"));
        when(mockTemplate.queryForList(anyString())).thenReturn(mockList);

        try {
            result = QueryDispatcher.checkPasswordValid(mockTemplate, 0, "password");
            Assertions.assertEquals(false, result);
        } catch (QueryException e) {
            Assertions.assertInstanceOf(BadCredentialsException.class, e.getCause());
        }
    }
}

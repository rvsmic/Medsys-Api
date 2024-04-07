package com.medsys.medsysapi.db;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class QueryDispatcherTests {

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Test
    public void testDispatch() {
        assertNotNull(jdbcTemplate);

        DriverManagerDataSource dataSource = (DriverManagerDataSource) jdbcTemplate.getDataSource();
        assertNotNull(dataSource);

        String table = "personnel";
        String column = "id";

        String result = QueryDispatcher.dispatch(jdbcTemplate, table, column);

        assertNotNull(result);
        assertEquals("[1, 2, 3]", result);
    }
}

package com.medsys.medsysapi.db;

import org.springframework.jdbc.core.JdbcTemplate;

public class QueryDispatcher {
    public static String dispatch(JdbcTemplate jdbcTemplate, String table, String column) {
        return jdbcTemplate.query("SELECT " + column + " FROM medsys." + table, (rs, rowNum) -> rs.getString(column)).toString();
    }
}

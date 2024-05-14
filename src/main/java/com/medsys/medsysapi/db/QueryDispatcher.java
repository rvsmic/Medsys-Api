package com.medsys.medsysapi.db;

import com.medsys.medsysapi.security.SecUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class QueryDispatcher {

    static Logger logger = LoggerFactory.getLogger(QueryDispatcher.class);

    public static QueryResults dispatch(JdbcTemplate jdbcTemplate, String table, String column) throws QueryException {
        try {
            QueryResults queryResults = new QueryResults();
            queryResults.getFromQuery(jdbcTemplate.queryForList("SELECT " + column + " FROM " + table + ";"));
            return queryResults;
        } catch (Throwable e) {
            throw new QueryException(e);
        }
    }

    public static QueryResults dispatch(JdbcTemplate jdbcTemplate, String table, String column, String condition) throws QueryException {
        try {
            QueryResults queryResults = new QueryResults();
            queryResults.getFromQuery(jdbcTemplate.queryForList("SELECT " + column + " FROM " + table + " WHERE " + condition + ";"));
            return queryResults;
        } catch (Throwable e) {
            throw new QueryException(e);
        }
    }

    public static SecUserDetails getSecUserDetails(JdbcTemplate jdbcTemplate, int id) throws QueryException {
        QueryResults queryResults = dispatch(jdbcTemplate, "personnel", "*", "id = " + id);
        return new SecUserDetails(queryResults.getFirstResult());
    }

    public static int getIdUsername(JdbcTemplate jdbcTemplate, String username) throws QueryException {
        QueryResults queryResults = dispatch(jdbcTemplate, "personnel", "id", "username = " + "\'" + username + "\'");
        String result = queryResults.getResultsAsString();

        if(result == "") {
            throw new QueryException(401, new UsernameNotFoundException("User " + username + " not found"));
        }

        else if(result.contains(",")) {
            throw new QueryException(new DuplicateKeyException("Multiple users with the same username"));
        }

        return Integer.parseInt(result);
    }

    public static Boolean checkPasswordValid(JdbcTemplate jdbcTemplate, int id, String password) throws QueryException {
        int result = -1;
        QueryResults queryResults = dispatch(jdbcTemplate, "personnel", "COUNT(*)", "id = "  + id  + " AND password = " + "\'" + password + "\'");

        result = Integer.parseInt(queryResults.getResultsAsString());

        // blad podczas sprawdzania hasla
        if(result == -1) {
            throw new QueryException(new Exception("Unknown error while checking password"));
        }

        // haslo niepoprawne
        else if(result == 0) {
            throw new QueryException(401, new BadCredentialsException("Invalid password"));
        }

        // zwrocono wiecej niz jeden wynik
        else if (result > 1) {
            throw new QueryException(new IllegalStateException("Multiple rows returned while checking password"));
        }

        // haslo poprawne
        else if (result == 1) {
            return true;
        }

        return false;
    }
}

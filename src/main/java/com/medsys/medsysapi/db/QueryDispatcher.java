package com.medsys.medsysapi.db;

import com.medsys.medsysapi.security.SecUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

@Service
public class QueryDispatcher {

    private final Logger logger = LoggerFactory.getLogger(QueryDispatcher.class);

    @Autowired
    DataSource dataSource;

    protected Connection connection;

    public QueryDispatcher() {
    }

    public QueryResults dispatch(String sql, String[] params) throws QueryException {
        try {
            connection = dataSource.getConnection();
            PreparedStatement p = connection.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                p.setString(i + 1, params[i]);
            }
            QueryResults queryResults = new QueryResults();
            queryResults.getFromResultSet(p.executeQuery());
            connection.close();
            return queryResults;
        } catch (Throwable e) {
            throw new QueryException(e);
        }
    }

    public SecUserDetails getSecUserDetails(int id) throws QueryException {
        String sql = String.format("SELECT * FROM personnel WHERE id = %d", id);
        String[] params = {};
        QueryResults queryResults = dispatch(sql, params);
        return new SecUserDetails(queryResults.getFirstResult());
    }

    public int getIdUsername(String username) throws QueryException {
        String sql = "SELECT id FROM personnel WHERE username = ?";
        String[] params = {username};
        QueryResults queryResults = dispatch(sql, params);
        String result = queryResults.getResultsAsString();

        if(result == "") {
            throw new QueryException(401, new UsernameNotFoundException("User " + username + " not found"));
        }

        else if(result.contains(",")) {
            throw new QueryException(new DuplicateKeyException("Multiple users with the same username"));
        }

        return Integer.parseInt(result);
    }

    public Boolean checkPasswordValid(int id, String password) throws QueryException {
        int result = -1;
        String hashedPassword = DigestUtils.md5DigestAsHex(password.getBytes()).toUpperCase();
        String sql = String.format("SELECT COUNT(*) FROM personnel WHERE id = %d AND password = ?", id);
        String[] params = {hashedPassword};
        QueryResults queryResults = dispatch(sql, params);

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

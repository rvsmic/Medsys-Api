package com.medsys.medsysapi.service.errors;

import com.medsys.medsysapi.db.QueryDispatcher;
import com.medsys.medsysapi.db.QueryException;
import com.medsys.medsysapi.model.Error;
import com.medsys.medsysapi.utils.ErrorResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.util.List;
import java.util.Map;

@Service
public class ErrorService {
    private final QueryDispatcher queryDispatcher;
    private final Logger logger = LoggerFactory.getLogger(ErrorService.class);

    @Autowired
    ErrorResponseHandler errorResponseHandler;

    @Autowired
    public ErrorService(QueryDispatcher queryDispatcher) {
        this.queryDispatcher = queryDispatcher;
    }

    public Map<String, Object> getError(int id) throws QueryException {
        String sql = "SELECT * FROM errors WHERE id = ?";
        Object[] params = {id};
        Map<String, Object> results = queryDispatcher.dispatch(sql, params).getFirstResult();
        Time time = (Time) results.get("time");
        String timeString = time.toString();
        String[] timeParts = timeString.split(":");
        String timeFormatted = timeParts[0] + ":" + timeParts[1];
        results.put("time", timeFormatted);
        return results;
    }

    public List<Map<String, Object>> getAllErrors() throws QueryException {
        String sql = "SELECT * from errors";
        Object[] params = {};
        List<Map<String, Object>> results = queryDispatcher.dispatch(sql, params).getResults();
        for(Map<String, Object> result : results) {
            Time time = (Time) result.get("godzina");
            String timeString = time.toString();
            String[] timeParts = timeString.split(":");
            String timeFormatted = timeParts[0] + ":" + timeParts[1];
            result.put("godzina", timeFormatted);
        }

        return results;
    }

    public void addError(Error error) throws QueryException {
        List<Map<String, Object>> errors = getAllErrors();
        int error_id = 1;
        while(true) {
            boolean found = false;
            for(Map<String, Object> a : errors) {
                if((int)a.get("id") == error_id) {
                    found = true;
                    break;
                }
            }
            if(!found) {
                break;
            }
            error_id++;
        }
        String sql = "INSERT INTO errors (id, title, description, date, time, resolved) VALUES (?, ?, ?, ?, ?, ?)";
        Object[] params = {error_id, error.getTitle(), error.getDescription(), error.getDate(), error.getTime(), false};
        queryDispatcher.dispatchUpdate(sql, params);

    }

    public void updateError(int id, Error error) throws QueryException {
        String sql = "UPDATE appointments SET title = ?, description = ?, date = ?, time = ?, resolved = ? WHERE id = ?";
        Object[] params = {error.getTitle(), error.getDescription(), error.getDate(), error.getTime(), error.getResolved(), id};
        queryDispatcher.dispatchUpdate(sql, params);
    }
}

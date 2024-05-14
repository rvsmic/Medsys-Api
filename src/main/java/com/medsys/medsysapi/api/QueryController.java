package com.medsys.medsysapi.api;

import com.medsys.medsysapi.db.QueryDispatcher;
import com.medsys.medsysapi.db.QueryException;
import com.medsys.medsysapi.db.QueryResults;
import com.medsys.medsysapi.utils.BasicResponse;
import com.medsys.medsysapi.utils.ErrorResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/query")
@CrossOrigin
public class QueryController {

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @GetMapping
    public ResponseEntity query(@RequestParam(name = "table") String table, @RequestParam(name = "column") String column) {
        try {
            QueryResults queryResults = QueryDispatcher.dispatch(jdbcTemplate, table, column);
            return new BasicResponse(200, "Query successful.", queryResults.getResults()).generateResponse();
        } catch (QueryException e) {
            return ErrorResponseHandler.generateErrorResponse(e.getStatus(), e);
        }
    }
}
// showcase class for querying the database

package com.medsys.medsysapi.api;

import com.medsys.medsysapi.db.QueryDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/query")
@CrossOrigin
public class QueryController {

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @GetMapping
    public String query(@RequestParam(name = "table") String table, @RequestParam(name = "column") String column) {
        return QueryDispatcher.dispatch(jdbcTemplate, table, column);
    }
}
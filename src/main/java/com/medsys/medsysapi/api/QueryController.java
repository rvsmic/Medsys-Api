package com.medsys.medsysapi.api;

import com.medsys.medsysapi.db.QueryDispatcher;
import com.medsys.medsysapi.db.QueryException;
import com.medsys.medsysapi.db.QueryResults;
import com.medsys.medsysapi.security.SecUser;
import com.medsys.medsysapi.security.SecUserDetailsService;
import com.medsys.medsysapi.security.SecUserRoles;
import com.medsys.medsysapi.utils.BasicResponse;
import com.medsys.medsysapi.utils.ErrorResponseHandler;
import com.medsys.medsysapi.utils.JsonHandler;
import jakarta.annotation.security.RolesAllowed;
import net.minidev.json.JSONObject;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/query")
@CrossOrigin
@RolesAllowed(SecUserRoles.ROLE_ADMIN)
public class QueryController {

    @Autowired
    public QueryDispatcher queryDispatcher;

    @Autowired
    public SecUserDetailsService secUserDetailsService;

    @PostMapping
    public ResponseEntity query(@RequestBody(required = true) String data) {
        try {
            JSONObject jsonData = JsonHandler.readJsonData(data);
            String token = (String) jsonData.get("token");
            String table = (String) jsonData.get("table");
            String column = (String) jsonData.get("column");

//            SecUser user = secUserDetailsService.loadUserByToken(token);

//            if(user.getAuthorities().contains(SecUserRoles.ROLE_ADMIN)) {
                String sql = String.format("SELECT %s FROM %s", column, table);
                String[] params = {};

                QueryResults queryResults = queryDispatcher.dispatch(sql, params);
                return new BasicResponse(200, "Query successful.", queryResults.getResults()).generateResponse();
//            } else {
//                return ErrorResponseHandler.generateErrorResponse(403, new Exception("You do not have permission to access this resource."));
//            }
        } catch (QueryException e) {
            return ErrorResponseHandler.generateErrorResponse(e.getStatus(), e);
        } catch (BadRequestException e) {
            return ErrorResponseHandler.generateErrorResponse(400, e);
        }
    }
}
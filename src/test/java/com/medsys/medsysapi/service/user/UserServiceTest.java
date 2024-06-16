package com.medsys.medsysapi.service.user;

import com.medsys.medsysapi.db.QueryDispatcher;
import com.medsys.medsysapi.db.QueryException;
import com.medsys.medsysapi.db.QueryResults;
import com.medsys.medsysapi.security.SecUserDetails;
import com.medsys.medsysapi.utils.BasicResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    QueryDispatcher queryDispatcher;
    @InjectMocks
    UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUserInfo() throws QueryException {
        when(queryDispatcher.getSecUserDetails(anyInt())).thenReturn(new SecUserDetails(0, "Shanera Yoshioka", new GregorianCalendar(2024, Calendar.MAY, 28, 13, 25).getTime(), "pesel", "gender", "phone_number", "address", "speciality", "username", "profession"));

        ResponseEntity result = userService.userInfo(0);
        ResponseEntity expected = new BasicResponse(200, "OK", new HashMap<String, Object>() {{
            put("system_id", 0);
            put("first_name", "Shanera");
            put("second_name", null);
            put("last_name", "Yoshioka");
            put("full_name", "Shanera Yoshioka");
            put("login", "username");
            put("dob", "28.05.2024");
            put("profession", "profession");
            put("speciality", "speciality");
        }}).generateResponse();
        System.out.println(result.getBody().toString());
        assertThat(result.getBody().toString()).isEqualTo(expected.getBody().toString());
    }

    @Test
    void testGetAllUsers() {
        QueryResults mockQueryResults = new QueryResults();
        List<Map<String, Object>> expected = List.of(Map.of("id", 0, "name", "name", "dob", new GregorianCalendar(2024, Calendar.MAY, 19, 6, 33).getTime(), "pesel", "pesel", "gender", "gender", "phone_number", "phone_number", "address", "address", "speciality", "speciality", "username", "username", "profession", "profession"));
        mockQueryResults.getFromList(expected);

        try {
            when(queryDispatcher.dispatch(anyString(), any(Object[].class))).thenReturn(mockQueryResults);
            List<SecUserDetails> result = userService.getAllUsers();
            Assertions.assertEquals(List.of(new SecUserDetails(expected.get(0))), result);
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }
}
package com.medsys.medsysapi.service.user;

import com.medsys.medsysapi.db.QueryDispatcher;
import com.medsys.medsysapi.db.QueryException;
import com.medsys.medsysapi.db.QueryResults;
import com.medsys.medsysapi.security.SecUserDetails;
import com.medsys.medsysapi.utils.BasicResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
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
        when(queryDispatcher.getSecUserDetails(anyInt()))
        .thenReturn(
                new SecUserDetails(
                        0,
                        "Shanera Yoshioka",
                        new GregorianCalendar(2024, Calendar.MAY, 28, 13, 25).getTime(),
                        "pesel",
                        "gender",
                        "phone_number",
                        "address",
                        "specialty",
                        "username",
                        "profession"
                )
        );

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
            put("specialty", "specialty");
            put("phone_number", "phone_number");
            put("address", "address");
            put("pesel", "pesel");
            put("gender", "gender");
        }}).generateResponse();
        System.out.println(result.getBody().toString());
        assertThat(result.getBody().toString()).isEqualTo(expected.getBody().toString());
    }

    @Test
    void testGetAllPersonnel() {
        QueryResults mockQueryResults = new QueryResults();
        List<Map<String, Object>> expected = List.of(Map.of("id", 0, "name", "name", "dob", new GregorianCalendar(2024, Calendar.MAY, 19, 6, 33).getTime(), "pesel", "pesel", "gender", "gender", "phone_number", "phone_number", "address", "address", "specialty", "specialty", "username", "username", "profession", "profession"));
        mockQueryResults.getFromList(expected);

        try {
            when(queryDispatcher.dispatch(anyString(), any(Object[].class))).thenReturn(mockQueryResults);
            List<SecUserDetails> result = userService.getAllPersonnel();
            Assertions.assertEquals(List.of(new SecUserDetails(expected.get(0))), result);
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void testGetUser() throws QueryException {
        Map<String, Object> mockResult = new HashMap<>();
        mockResult.put("id", 0);
        mockResult.put("name", "Shanera Yoshioka");
        mockResult.put("date_of_birth", new Date(new GregorianCalendar(2024, Calendar.MAY, 28, 13, 25).getTimeInMillis()));
        mockResult.put("pesel", "pesel");
        mockResult.put("gender", "gender");
        mockResult.put("phone_number", "phone_number");
        mockResult.put("address", "address");
        mockResult.put("specialty", "specialty");
        mockResult.put("username", "username");
        mockResult.put("profession", "profession");

        QueryResults mockQueryResults = Mockito.mock(QueryResults.class);
        when(mockQueryResults.getFirstResult()).thenReturn(mockResult);
        when(queryDispatcher.dispatch(anyString(), any(Object[].class))).thenReturn(mockQueryResults);

        SecUserDetails result = userService.getUser(0);
        Assertions.assertEquals(new SecUserDetails(mockResult), result);
    }

    @Test
    void testGetAllUsersLabeled() throws QueryException {
        List<Map<String, Object>> mockResults = List.of(
                Map.of("id", 1, "name", "John Doe"),
                Map.of("id", 2, "name", "Jane Doe")
        );

        QueryResults mockQueryResults = Mockito.mock(QueryResults.class);
        when(mockQueryResults.getResults()).thenReturn(mockResults);
        when(queryDispatcher.dispatch(anyString(), any(Object[].class))).thenReturn(mockQueryResults);

        List<Map<String, Object>> result = userService.getAllUsersLabeled();
        List<Map<String, Object>> expected = List.of(
                Map.of("value", 1, "label", "John Doe"),
                Map.of("value", 2, "label", "Jane Doe")
        );

        Assertions.assertEquals(expected, result);
    }

    @Test
    void testGetAllDoctorsLabeled() throws QueryException {
        List<Map<String, Object>> mockResults = List.of(
                Map.of("id", 1, "name", "Dr. John Doe"),
                Map.of("id", 2, "name", "Dr. Jane Doe")
        );

        QueryResults mockQueryResults = Mockito.mock(QueryResults.class);
        when(mockQueryResults.getResults()).thenReturn(mockResults);
        when(queryDispatcher.dispatch(anyString(), any(Object[].class))).thenReturn(mockQueryResults);

        List<Map<String, Object>> result = userService.getAllDoctorsLabeled();
        List<Map<String, Object>> expected = List.of(
                Map.of("value", 1, "label", "Dr. John Doe"),
                Map.of("value", 2, "label", "Dr. Jane Doe")
        );

        Assertions.assertEquals(expected, result);
    }

    @Test
    void testGetAllPersonnelLabeled() throws QueryException {
        List<Map<String, Object>> mockResults = List.of(
                Map.of("id", 1, "name", "John Doe"),
                Map.of("id", 2, "name", "Jane Doe")
        );

        QueryResults mockQueryResults = Mockito.mock(QueryResults.class);
        when(mockQueryResults.getResults()).thenReturn(mockResults);
        when(queryDispatcher.dispatch(anyString(), any(Object[].class))).thenReturn(mockQueryResults);

        List<Map<String, Object>> result = userService.getAllPersonnelLabeled();
        List<Map<String, Object>> expected = List.of(
                Map.of("value", 1, "label", "John Doe"),
                Map.of("value", 2, "label", "Jane Doe")
        );

        Assertions.assertEquals(expected, result);
    }

    @Test
    void testGetAllAdminsLabeled() throws QueryException {
        List<Map<String, Object>> mockResults = List.of(
                Map.of("id", 1, "name", "Admin John Doe"),
                Map.of("id", 2, "name", "Admin Jane Doe")
        );

        QueryResults mockQueryResults = Mockito.mock(QueryResults.class);
        when(mockQueryResults.getResults()).thenReturn(mockResults);
        when(queryDispatcher.dispatch(anyString(), any(Object[].class))).thenReturn(mockQueryResults);

        List<Map<String, Object>> result = userService.getAllAdminsLabeled();
        List<Map<String, Object>> expected = List.of(
                Map.of("value", 1, "label", "Admin John Doe"),
                Map.of("value", 2, "label", "Admin Jane Doe")
        );

        Assertions.assertEquals(expected, result);
    }

    @Test
    void testAddUser() throws QueryException {
        SecUserDetails userDetails = new SecUserDetails(
                0,
                "Shanera Yoshioka",
                new GregorianCalendar(2024, Calendar.MAY, 28, 13, 25).getTime(),
                "pesel",
                "gender",
                "phone_number",
                "address",
                "specialty",
                "username",
                "profession"
        );

        when(queryDispatcher.dispatch(anyString(), any(Object[].class))).thenReturn(new QueryResults());

        userService.addUser(userDetails, "password");

        ArgumentCaptor<Object[]> captor = ArgumentCaptor.forClass(Object[].class);
        verify(queryDispatcher).dispatchUpdate(anyString(), captor.capture());

        Object[] params = captor.getValue();
        Assertions.assertEquals(11, params.length);
        Assertions.assertEquals(userDetails.getName(), params[1]);
    }

    @Test
    void testUpdateUser() throws QueryException {
        SecUserDetails userDetails = new SecUserDetails(
                0,
                "Shanera Yoshioka",
                new GregorianCalendar(2024, Calendar.MAY, 28, 13, 25).getTime(),
                "pesel",
                "gender",
                "phone_number",
                "address",
                "specialty",
                "username",
                "profession"
        );

        userService.updateUser(0, userDetails, "password");

        ArgumentCaptor<Object[]> captor = ArgumentCaptor.forClass(Object[].class);
        verify(queryDispatcher).dispatchUpdate(anyString(), captor.capture());

        Object[] params = captor.getValue();
        Assertions.assertEquals(11, params.length);
        Assertions.assertEquals(userDetails.getName(), params[0]);
    }

    @Test
    void testDeleteUser() throws QueryException {
        userService.deleteUser(0);

        ArgumentCaptor<Object[]> captor = ArgumentCaptor.forClass(Object[].class);
        verify(queryDispatcher).dispatchUpdate(anyString(), captor.capture());

        Object[] params = captor.getValue();
        Assertions.assertEquals(1, params.length);
        Assertions.assertEquals(0, params[0]);
    }
}
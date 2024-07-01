package com.medsys.medsysapi.service.errors;

import com.medsys.medsysapi.db.QueryDispatcher;
import com.medsys.medsysapi.db.QueryException;
import com.medsys.medsysapi.db.QueryResults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@SpringBootTest
class ErrorServiceTest {
    @Mock
    QueryDispatcher queryDispatcher;

    private ErrorService errorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        errorService = new ErrorService(queryDispatcher);
    }

    @Test
    void testGetAllErrors() {
        try {
            List<Map<String, Object>> expected = new ArrayList<>();
            expected.add(Map.of("id", 0, "time", "09:00"));

            QueryResults mockQueryResults = new QueryResults();
            List<Map<String, Object>> mocked = new ArrayList<>();
            Map<String, Object> mockedMap = new HashMap<>();
            mockedMap.put("id", 0);
            mockedMap.put("time", Time.valueOf("9:00:00"));
            mocked.add(mockedMap);
            mockQueryResults.getFromList(mocked);
            when(queryDispatcher.dispatch(anyString(), any(Object[].class))).thenReturn(mockQueryResults);

            List<Map<String, Object>> result = errorService.getAllErrors();
            Assertions.assertEquals(expected, result);
        } catch (QueryException e) {
            Assertions.fail(e.getMessage());
        }
    }
}
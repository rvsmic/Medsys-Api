package com.medsys.medsysapi.db;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class QueryResultsTest {
    @Mock
    List<Map<String, Object>> results;
    @InjectMocks
    QueryResults queryResults;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFromQuery() {
        queryResults.getFromQuery(List.of(Map.of("r", "r")));
        Assertions.assertEquals(List.of(Map.of("r", "r")), queryResults.getResults());
    }

    @Test
    void testGetResults() {
        assertNotNull(results);
        when(results.size()).thenReturn(0);
        when(results.isEmpty()).thenReturn(true);

        List<Map<String, Object>> result = queryResults.getResults();
        Assertions.assertEquals(0, result.size());

        when(results.size()).thenReturn(1);
        when(results.isEmpty()).thenReturn(false);
        when(results.get(anyInt())).thenReturn(Map.of("key", "value"));

        QueryResults testResults = new QueryResults();
        List<Map<String, Object>> testList = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            testList.add(Map.of(String.format("key%d", i), String.format("value%d", i)));
        }

        testResults.getFromQuery(testList);
        result = testResults.getResults();
        Assertions.assertEquals(10, result.size());
    }

    @Test
    void testGetResultsAsString() {
        assertNotNull(results);
        when(results.size()).thenReturn(0);
        when(results.isEmpty()).thenReturn(true);

        String result = queryResults.getResultsAsString();
        Assertions.assertEquals("", result);

        when(results.size()).thenReturn(1);
        when(results.isEmpty()).thenReturn(false);
        when(results.get(anyInt())).thenReturn(Map.of("key", "value"));

        QueryResults testResults = new QueryResults();
        List<Map<String, Object>> testList = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            testList.add(Map.of(String.format("key%d", i), String.format("value%d", i)));
        }

        testResults.getFromQuery(testList);
        result = testResults.getResultsAsString();
        Assertions.assertEquals("value0, value1, value2, value3, value4, value5, value6, value7, value8, value9", result);
    }

    @Test
    void testGetFirstResult() {
        when(results.isEmpty()).thenReturn(false);
        when(results.get(anyInt())).thenReturn(Map.of("key", "value"));

        Map<String, Object> result = queryResults.getFirstResult();
        Assertions.assertEquals(Map.of("key", "value"), result);
    }

    @Test
    void testGetResultsAt() {
        when(results.isEmpty()).thenReturn(false);
        when(results.get(anyInt())).thenReturn(Map.of("key", "value"));

        Map<String, Object> result = queryResults.getResultsAt(99);
        Assertions.assertEquals(Map.of("key", "value"), result);
    }
}
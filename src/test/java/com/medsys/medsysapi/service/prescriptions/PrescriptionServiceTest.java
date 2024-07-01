package com.medsys.medsysapi.service.prescriptions;

import com.medsys.medsysapi.db.QueryDispatcher;
import com.medsys.medsysapi.db.QueryException;
import com.medsys.medsysapi.db.QueryResults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@SpringBootTest
class PrescriptionServiceTest {
    @Mock
    QueryDispatcher queryDispatcher;

    private PrescriptionService prescriptionsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        prescriptionsService = new PrescriptionService(queryDispatcher);
    }

    @Test
    void testGetAllPatientsLabeled() {
        try {
            List<Map<String, Object>> expected = new ArrayList<>();
            expected.add(Map.of("value", 0, "label", "rec. 0-01/01/2021"));
            expected.add(Map.of("value", 1, "label", "rec. 1-02/01/2021"));

            QueryResults mockQueryResults = new QueryResults();
            List<Map<String, Object>> mocked = new ArrayList<>();
            mocked.add(Map.of("id", 0, "prescription_date", Date.valueOf("2021-01-01")));
            mocked.add(Map.of("id", 1, "prescription_date", Date.valueOf("2021-01-02")));
            mockQueryResults.getFromList(mocked);
            when(queryDispatcher.dispatch(anyString(), any(Object[].class))).thenReturn(mockQueryResults);

            List<Map<String, Object>> result = prescriptionsService.getAllPrescriptionsLabeled();
            Assertions.assertEquals(expected, result);
        } catch (QueryException e) {
            Assertions.fail(e.getMessage());
        }
    }
}
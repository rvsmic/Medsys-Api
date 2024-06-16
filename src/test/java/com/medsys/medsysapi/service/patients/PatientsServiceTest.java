package com.medsys.medsysapi.service.patients;

import com.medsys.medsysapi.db.QueryDispatcher;
import com.medsys.medsysapi.db.QueryResults;
import com.medsys.medsysapi.model.Patient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@SpringBootTest
class PatientsServiceTest {
    @Mock
    QueryDispatcher queryDispatcher;

    private PatientsService patientsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        patientsService = new PatientsService(queryDispatcher);
    }

    @Test
    void testGetAllPatients() {
        QueryResults mockQueryResults = new QueryResults();
        List<Map<String, Object>> expected = List.of(Map.of("id", 0, "name", "name", "dob", new Date(0), "pesel", "pesel", "gender", "gender", "phone_number", "phone_number", "address", "address", "follow_up_date", new Date(0), "blood_type", "blood_type"));
        mockQueryResults.getFromList(expected);

        try {
            when(queryDispatcher.dispatch(anyString(), any(Object[].class))).thenReturn(mockQueryResults);
            List<Patient> result = patientsService.getAllPatients();
            Assertions.assertEquals(List.of(new Patient(expected.get(0))), result);
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }
}
package com.medsys.medsysapi.service.appointments;

import com.medsys.medsysapi.db.QueryDispatcher;
import com.medsys.medsysapi.db.QueryException;
import com.medsys.medsysapi.db.QueryResults;
import com.medsys.medsysapi.model.Appointment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@SpringBootTest
class AppointmentServiceTest {
    @Mock
    QueryDispatcher queryDispatcher;

    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        appointmentService = new AppointmentService(queryDispatcher);
    }

    @Test
    void testGetAllAppointmentsLabeled() {
        try {
            List<Map<String, Object>> expected = new ArrayList<>();
            expected.add(Map.of("value", 0, "label", "label_0"));
            expected.add(Map.of("value", 1, "label", "label_1"));

            QueryResults mockQueryResults = new QueryResults();
            List<Map<String, Object>> mocked = new ArrayList<>();
            mocked.add(Map.of("id", 0, "appointment_name", "label_0"));
            mocked.add(Map.of("id", 1, "appointment_name", "label_1"));
            mockQueryResults.getFromList(mocked);
            when(queryDispatcher.dispatch(anyString(), any(Object[].class))).thenReturn(mockQueryResults);

            List<Map<String, Object>> result = appointmentService.getAllAppointmentsLabeled();
            Assertions.assertEquals(expected, result);
        } catch (QueryException e) {
            Assertions.fail(e.getMessage());
        }
    }
}
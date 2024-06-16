package com.medsys.medsysapi.service.lab_tests;

import com.medsys.medsysapi.db.QueryDispatcher;
import com.medsys.medsysapi.db.QueryException;
import com.medsys.medsysapi.model.LabTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LabTestService {
    private final QueryDispatcher queryDispatcher;
    private final Logger logger = LoggerFactory.getLogger(LabTestService.class);

    @Autowired
    public LabTestService(QueryDispatcher queryDispatcher) {
        this.queryDispatcher = queryDispatcher;
    }

    public LabTest getLabTest(int id) throws QueryException {
        String sql = "SELECT * FROM lab_tests WHERE id = ?";
        Object[] params = {id};
        return new LabTest(queryDispatcher.dispatch(sql, params).getFirstResult());
    }

    public List<LabTest> getAllLabTests() throws QueryException {
        String sql = "SELECT * FROM lab_tests";
        Object[] params = {};
        List<Map<String, Object>> results = queryDispatcher.dispatch(sql, params).getResults();
        List<LabTest> labTests = new ArrayList<>();
        for(Map<String, Object> result : results) {
            labTests.add(new LabTest(result));
        }
        return labTests;
    }

    public List<Map<String, Object>> getAllLabTestsLabeled() throws QueryException {
        String sql = "SELECT id, test_result FROM lab_tests";
        Object[] params = {};
        List<Map<String, Object>> results = queryDispatcher.dispatch(sql, params).getResults();
        List<Map<String, Object>> labeled = new ArrayList<>();
        for(Map<String, Object> result : results) {
            labeled.add(Map.of("value", result.get("id"), "label", result.get("test_result")));
        }
        return labeled;
    }

    public void addLabTest(LabTest labTest) throws QueryException {
        List<LabTest> labTests = getAllLabTests();
        int labTest_id = 1;
        while (true) {
            boolean found = false;
            for (LabTest p : labTests) {
                if (p.getId() == labTest_id) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                break;
            }
            labTest_id++;
        }
        String sql = "INSERT INTO lab_tests (id, patient_id, test_date, test_type, test_result) VALUES (?, ?, ?, ?, ?)";
        Object[] params = {labTest_id, labTest.getPatient_id(), labTest.getTest_date(), labTest.getTest_type(), labTest.getTest_result()};
        queryDispatcher.dispatch(sql, params);
    }

    public void updateLabTest(int id, LabTest labTest) throws QueryException {
        String sql = "UPDATE lab_tests SET patient_id = ?, test_date = ?, test_type = ?, test_result = ? WHERE id = ?";
        Object[] params = {labTest.getPatient_id(), labTest.getTest_date(), labTest.getTest_type(), labTest.getTest_result(), id};
        queryDispatcher.dispatch(sql, params);
    }

    public void deleteLabTest(int id) throws QueryException {
        String sql = "DELETE FROM lab_tests WHERE id = ?";
        Object[] params = {id};
        queryDispatcher.dispatch(sql, params);
    }
}

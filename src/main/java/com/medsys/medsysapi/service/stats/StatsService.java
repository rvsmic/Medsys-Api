package com.medsys.medsysapi.service.stats;

import com.medsys.medsysapi.db.QueryDispatcher;
import com.medsys.medsysapi.db.QueryException;
import com.medsys.medsysapi.model.Appointment;
import com.medsys.medsysapi.model.LabTest;
import com.medsys.medsysapi.model.Prescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StatsService {

    private final StatsTracker statsTracker;
    private final QueryDispatcher queryDispatcher;
    private final Logger logger = LoggerFactory.getLogger(StatsService.class);

    @Autowired
    public StatsService(StatsTracker statsTracker, QueryDispatcher queryDispatcher) {
        this.statsTracker = statsTracker;
        this.queryDispatcher = queryDispatcher;
        updateActions();
    }

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void removeExpiredStats() {
        statsTracker.removeExpiredActions();
        logger.info("Expired stats removed");
    }

    public void removeUserStats(int userId) {
        statsTracker.removeUserStats(userId);
    }

    public void recordUserAction(int userId, ActionType action) {
        statsTracker.incrementUserStat(userId, 0, action);
    }

    public void recordUserAction(int userId, ActionType action, Date date) {
        statsTracker.incrementUserStat(userId, 0, action, date);
    }

    public void recordUserAction(int userId, int actionID, ActionType action) {
        statsTracker.incrementUserStat(userId, actionID, action);
    }

    public void recordUserAction(int userId, int actionID, ActionType action, Date date) {
        statsTracker.incrementUserStat(userId, actionID, action, date);
    }

    public void recordUserActionDistinct(int userId, int actionID, ActionType action) {
        statsTracker.incrementUserStatDistinct(userId, actionID, action);
    }

    public void recordUserActionDistinct(int userId, int actionID, ActionType action, Date date) {
        statsTracker.incrementUserStatDistinct(userId, actionID, action, date);
    }

    public Map<String, Integer> getUserDailyStatistics(int userId) {
        Map<String, Integer> dailyStats = new ConcurrentHashMap<>();
        statsTracker.getUserDailyStats(userId).forEach((actionType, userActions) -> dailyStats.put(actionType.toString(), userActions.size()));
        return dailyStats;
    }

    public List<Map<String, Object>> getUserDailyStatisticsLabeled(int userId) {
        List<Map<String, Object>> dailyStatsLabeled = new ArrayList<>();
        Map<String, Integer> dailyStats = getUserDailyStatistics(userId);
        dailyStats.forEach((actionType, count) -> {
            dailyStatsLabeled.add(Map.of("label", actionType, "value", count));
        });

        return dailyStatsLabeled;
    }

    public Map<String, Integer> getUserWeeklyStatistics(int userId) {
        Map<String, Integer> weeklyStats = new ConcurrentHashMap<>();
        statsTracker.getUserWeeklyStats(userId).forEach((actionType, userActions) -> weeklyStats.put(actionType.toString(), userActions.size()));
        return weeklyStats;
    }

    public List<Map<String, Object>> getUserWeeklyStatisticsLabeled(int userId) {
        List<Map<String, Object>> weeklyStatsLabeled = new ArrayList<>();
        Map<String, Integer> weeklyStats = getUserWeeklyStatistics(userId);
        weeklyStats.forEach((actionType, count) -> {
            weeklyStatsLabeled.add(Map.of("label", actionType, "value", count));
        });

        return weeklyStatsLabeled;
    }

    void updateActions() {
        try {
            List<Integer> doctorIds = getDoctorsIds();
            for (int userId : doctorIds) {
                statsTracker.setUpActionTypesForDoctor(userId);
                List<Appointment> appointments = getDoctorAppointments(userId);
                for (Appointment appointment : appointments) {
                    if(appointment.appointment_status.equals("Z")) {
                        recordUserActionDistinct(userId, appointment.id, ActionType.DOCTOR_REMAINING_APPOINTMENT, new Date(appointment.appointment_date.getTime() + appointment.appointment_time.getTime()));
                    } else if(appointment.appointment_status.equals("O")) {
                        recordUserActionDistinct(userId, appointment.id, ActionType.DOCTOR_CANCELLED_APPOINTMENT, new Date(appointment.appointment_date.getTime() + appointment.appointment_time.getTime()));
                    }
                }

                List<Prescription> prescriptions = getDoctorPrescriptions(userId);
                for (Prescription prescription : prescriptions) {
                    recordUserActionDistinct(userId, prescription.id, ActionType.DOCTOR_PRESCRIPTION_ISSUED, prescription.prescription_date);
                }

                List<LabTest> labTests = getDoctorLabTests(userId);
                for (LabTest labTest : labTests) {
                    recordUserActionDistinct(userId, labTest.id, ActionType.DOCTOR_TEST_RESULT, labTest.test_date);
                }
            }
            logger.info("Doctor actions updated");

            List<Integer> personnelIds = getPersonnelIds();
            for(int userId : personnelIds) {
                statsTracker.setUpActionTypesForPersonnel(userId);
            }
            logger.info("Personnel actions updated");

        } catch (QueryException e) {
            logger.error("Error at StatsService::addDefinedActions: " + e.getMessage() + " caused by " + e.getCause());
        }
    }

    private List<Integer> getDoctorsIds() throws QueryException {
        List<Integer> userIds = new ArrayList<>();

        String sql = "SELECT id FROM personnel WHERE profession = 'Lekarz'";
        String[] params = {};

        queryDispatcher.dispatch(sql, params).getResults().forEach(result -> {
            if(result.containsKey("id")) {
                userIds.add((int) result.get("id"));
            }
        });

        return userIds;
    }

    private List<Integer> getPersonnelIds() throws QueryException {
        List<Integer> userIds = new ArrayList<>();

        String sql = "SELECT id FROM personnel WHERE profession = 'Personel'";
        String[] params = {};

        queryDispatcher.dispatch(sql, params).getResults().forEach(result -> {
            if(result.containsKey("id")) {
                userIds.add((int) result.get("id"));
            }
        });

        return userIds;
    }

    private List<Appointment> getDoctorAppointments(int doctorId) throws QueryException {
        List<Appointment> appointments = new ArrayList<>();

        String sql = "SELECT * FROM appointments WHERE doctor_id = ?";
        Object[] params = {doctorId};

        queryDispatcher.dispatch(sql, params).getResults().forEach(result -> {
            appointments.add(new Appointment(result));
        });

        return appointments;
    }

    private List<Prescription> getDoctorPrescriptions(int doctorId) throws QueryException {
        List<Prescription> prescriptions = new ArrayList<>();

        String sql = "SELECT * FROM prescriptions WHERE doctor_id = ?";
        Object[] params = {doctorId};

        queryDispatcher.dispatch(sql, params).getResults().forEach(result -> {
            prescriptions.add(new Prescription(result));
        });

        return prescriptions;
    }

    private List<LabTest> getDoctorLabTests(int doctorId) throws QueryException {
        List<LabTest> labTests = new ArrayList<>();

        String sql = "SELECT * FROM lab_tests WHERE patient_id IN (SELECT patient_id FROM appointments WHERE doctor_id = ?)";
        Object[] params = {doctorId};

        queryDispatcher.dispatch(sql, params).getResults().forEach(result -> {
            labTests.add(new LabTest(result));
        });

        return labTests;
    }
}
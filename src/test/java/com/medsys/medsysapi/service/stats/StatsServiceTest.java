package com.medsys.medsysapi.service.stats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.mockito.Mockito.*;

@SpringBootTest
class StatsServiceTest {
    @Autowired
    StatsService statsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        statsService.removeUserStats(-1);
    }

    @Test
    void testGetUserDailyStatistics() {
        Assertions.assertEquals(0, statsService.getUserDailyStatistics(-1).size());

        statsService.recordUserAction(-1, ActionType.PERSONNEL_ADDED_PERSONNEL);
        statsService.recordUserAction(-1, ActionType.PERSONNEL_ADDED_PERSONNEL);
        statsService.recordUserAction(-1, ActionType.PERSONNEL_ADDED_PERSONNEL, new Date(0));

        Assertions.assertEquals(1, statsService.getUserDailyStatistics(-1).size());
        Assertions.assertEquals(Map.of(ActionType.PERSONNEL_ADDED_PERSONNEL, 2).toString(), statsService.getUserDailyStatistics(-1).toString());
    }

    @Test
    void testGetUserDailyStatisticsLabeled() {
        Assertions.assertEquals(0, statsService.getUserDailyStatisticsLabeled(-1).size());

        statsService.recordUserAction(-1, ActionType.PERSONNEL_ADDED_APPOINTMENT);
        statsService.recordUserAction(-1, ActionType.PERSONNEL_ADDED_APPOINTMENT);
        statsService.recordUserAction(-1, ActionType.PERSONNEL_ADDED_APPOINTMENT, new Date(0));

        Assertions.assertEquals(1, statsService.getUserDailyStatisticsLabeled(-1).size());
        Assertions.assertEquals(Map.of("label", ActionType.PERSONNEL_ADDED_APPOINTMENT.toString(), "value", 2), statsService.getUserDailyStatisticsLabeled(-1).get(0));
    }

    @Test
    void testGetUserWeeklyStatistics() {
        Assertions.assertEquals(0, statsService.getUserWeeklyStatistics(-1).size());

        statsService.recordUserAction(-1, ActionType.PERSONNEL_ADDED_PATIENT);
        statsService.recordUserAction(-1, ActionType.PERSONNEL_ADDED_PATIENT);
        statsService.recordUserAction(-1, ActionType.PERSONNEL_ADDED_PATIENT, new Date(0));

        Assertions.assertEquals(1, statsService.getUserWeeklyStatistics(-1).size());
        Assertions.assertEquals(Map.of(ActionType.PERSONNEL_ADDED_PATIENT, 2).toString(), statsService.getUserWeeklyStatistics(-1).toString());
    }

    @Test
    void testGetUserWeeklyStatisticsLabeled() {
        Assertions.assertEquals(0, statsService.getUserWeeklyStatisticsLabeled(-1).size());

        statsService.recordUserAction(-1, ActionType.PERSONNEL_BUG_REPORTED);
        statsService.recordUserAction(-1, ActionType.PERSONNEL_BUG_REPORTED);
        statsService.recordUserAction(-1, ActionType.PERSONNEL_BUG_REPORTED, new Date(0));

        Assertions.assertEquals(1, statsService.getUserWeeklyStatisticsLabeled(-1).size());
        Assertions.assertEquals(Map.of("label", ActionType.PERSONNEL_BUG_REPORTED.toString(), "value", 2), statsService.getUserWeeklyStatisticsLabeled(-1).get(0));
    }
}
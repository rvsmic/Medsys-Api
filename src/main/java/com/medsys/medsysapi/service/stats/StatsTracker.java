package com.medsys.medsysapi.service.stats;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StatsTracker {
    private final Map<Integer, UserStats> userStats = new ConcurrentHashMap<>();

    public void setUpActionTypesForDoctor(int userId) {
        userStats.computeIfAbsent(userId, k -> new UserStats()).setUpActionTypesForDoctors();
    }

    public void setUpActionTypesForPersonnel(int userId) {
        userStats.computeIfAbsent(userId, k -> new UserStats()).setUpActionTypesForPersonnel();
    }

    public void incrementUserStat(int userId, int actionID, ActionType action) {
        userStats.computeIfAbsent(userId, k -> new UserStats()).addAction(actionID, action);
    }

    public void incrementUserStat(int userId, int actionID, ActionType action, Date date) {
        userStats.computeIfAbsent(userId, k -> new UserStats()).addAction(actionID, action, date);
    }

    public void incrementUserStatDistinct(int userId, int actionID, ActionType action) {
        userStats.computeIfAbsent(userId, k -> new UserStats()).addActionDistinct(actionID, action);
    }

    public void incrementUserStatDistinct(int userId, int actionID, ActionType action, Date date) {
        userStats.computeIfAbsent(userId, k -> new UserStats()).addActionDistinct(actionID, action, date);
    }

    public void removeExpiredActions() {
        userStats.forEach((k, v) -> v.removeExpiredActions());
    }

    public void removeUserStats(int userId) {
        userStats.remove(userId);
    }

    public Map<ActionType, List<UserAction>> getUserDailyStats(int userId) {
        UserStats stats = userStats.get(userId);
        return stats != null ? stats.getDailyStats() : new ConcurrentHashMap<>();
    }

    public Map<ActionType, List<UserAction>> getUserWeeklyStats(int userId) {
        UserStats stats = userStats.get(userId);
        return stats != null ? stats.getWeeklyStats() : new ConcurrentHashMap<>();
    }
}

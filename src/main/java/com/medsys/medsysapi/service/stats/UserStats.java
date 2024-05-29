package com.medsys.medsysapi.service.stats;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserStats {

    private final Map<ActionType, List<UserAction>> actions = new ConcurrentHashMap<>();

    public void setUpActionTypesForDoctors() {
        actions.put(ActionType.DOCTOR_REMAINING_APPOINTMENT, new ArrayList<>());
        actions.put(ActionType.DOCTOR_CANCELLED_APPOINTMENT, new ArrayList<>());
        actions.put(ActionType.DOCTOR_PRESCRIPTION_ISSUED, new ArrayList<>());
        actions.put(ActionType.DOCTOR_TEST_RESULT, new ArrayList<>());

    }

    public void setUpActionTypesForPersonnel() {
        actions.put(ActionType.PERSONNEL_ADDED_PERSONNEL, new ArrayList<>());
        actions.put(ActionType.PERSONNEL_ADDED_APPOINTMENT, new ArrayList<>());
        actions.put(ActionType.PERSONNEL_ADDED_PATIENT, new ArrayList<>());
        actions.put(ActionType.PERSONNEL_BUG_REPORTED, new ArrayList<>());
    }

    public void addAction(int actionID, ActionType type) {
        if(actions.containsKey(type)) {
            actions.get(type).add(new UserAction(actionID, new Date(System.currentTimeMillis())));
        } else {
            actions.put(type, new ArrayList<>(List.of(new UserAction(actionID, new Date(System.currentTimeMillis())))));
        }
    }

    public void addAction(int actionID, ActionType type, Date date) {
        if(actions.containsKey(type)) {
            actions.get(type).add(new UserAction(actionID,date));
        } else {
            actions.put(type, new ArrayList<>(List.of(new UserAction(actionID, date))));
        }
    }

    public void addActionDistinct(int actionID, ActionType type) {
        if(actions.containsKey(type)) {
            if(actions.get(type).stream().noneMatch(action -> action.id == actionID)) {
                actions.get(type).add(new UserAction(actionID, new Date(System.currentTimeMillis())));
            }
        } else {
            actions.put(type, new ArrayList<>(List.of(new UserAction(actionID, new Date(System.currentTimeMillis())))));
        }
    }

    public void addActionDistinct(int actionID, ActionType type, Date date) {
        if(actions.containsKey(type)) {
            if(actions.get(type).stream().noneMatch(action -> action.id == actionID)) {
                actions.get(type).add(new UserAction(actionID, date));
            }
        }
    }

    public void removeExpiredActions() {
        actions.forEach((k, v) -> v.removeIf(action -> action.date.before(new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000))));
    }

    public Map<ActionType, List<UserAction>> getDailyStats() {
        Map<ActionType, List<UserAction>> dailyStats = new HashMap<>();
        for(Map.Entry<ActionType, List<UserAction>> entry : actions.entrySet()) {
            dailyStats.put(entry.getKey(), new ArrayList<>());
            for(UserAction action : entry.getValue()) {
                if(action.date.after(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000)) && action.date.before(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))) {
                    dailyStats.get(entry.getKey()).add(action);
                }
            }
        }
        return dailyStats;
    }

    public Map<ActionType, List<UserAction>> getWeeklyStats() {
        Map<ActionType, List<UserAction>> weeklyStats = new HashMap<>();
        for(Map.Entry<ActionType, List<UserAction>> entry : actions.entrySet()) {
            weeklyStats.put(entry.getKey(), new ArrayList<>());
            for(UserAction action : entry.getValue()) {
                if(action.date.after(new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000)) && action.date.before(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))) {
                    weeklyStats.get(entry.getKey()).add(action);
                }
            }
        }
        return weeklyStats;
    }
}
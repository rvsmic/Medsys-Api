package com.medsys.medsysapi.service.stats;

public enum ActionType {
    PERSONNEL_ADDED_APPOINTMENT("Dodane wizyty"),
    PERSONNEL_ADDED_PATIENT("Dodani pacjenci"),
    PERSONNEL_ADDED_PERSONNEL("Dodany personel"),
    PERSONNEL_BUG_REPORTED("Zgłoszone błędy"),
    DOCTOR_REMAINING_APPOINTMENT("Pozostałe wizyty"),
    DOCTOR_CANCELLED_APPOINTMENT("Odwołane wizyty"),
    DOCTOR_PRESCRIPTION_ISSUED("Wypisane recepty"),
    DOCTOR_TEST_RESULT("Wyniki testów");

    private String name;

    ActionType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

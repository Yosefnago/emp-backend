package com.ms.sw.user.model;

public enum ActionType {
    ADD("נוסף"),
    DELETE("מחק"),
    UPDATE("עידכן"),
    ARCHIVE("ארכיון"),
    RESTORE("שחזר");

    private final String hebrewLabel;

    ActionType(String hebrewLabel) {
        this.hebrewLabel = hebrewLabel;
    }
    public String getHebrewLabel() {
        return hebrewLabel;
    }
}

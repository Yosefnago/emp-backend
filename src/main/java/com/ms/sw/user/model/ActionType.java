package com.ms.sw.user.model;

import lombok.Getter;

@Getter
public enum ActionType {
    ADD("נוסף"),
    DELETE("מחק"),
    UPDATE("עידכן"),
    ARCHIVE("ארכיון"),
    RESTORE("שחזר"),
    SENT_TO_PAYROLL("נשלח עיבוד שכר"),
    GENERATED_PAYROLL("נשלח תלוש שכר");

    private final String hebrewLabel;

    ActionType(String hebrewLabel) {
        this.hebrewLabel = hebrewLabel;
    }
}

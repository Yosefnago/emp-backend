package com.ms.sw.employee.service;

public class PayrollConstants {

    private PayrollConstants() {
        throw new UnsupportedOperationException("Constant class");
    }

    // payroll constant
    public static final double DAILY_TRAVEL_RATE = 22.60;
    public static final double PENSION_EMPLOYEE_RATE = 0.06;
    public static final double PENSION_EMPLOYER_RATE = 0.065;
    public static final double SEVERANCE_EMPLOYER_RATE = 0.0833;

    // National Insurance Constants (NI & Health Tax)
    public static final double NI_INCOME_THRESHOLD = 7522.0;
    public static final double NI_EMPLOYEE_REDUCED_RATE = 0.035;
    public static final double NI_EMPLOYEE_FULL_RATE = 0.12;
    public static final double NI_EMPLOYER_REDUCED_RATE = 0.0355;
    public static final double NI_EMPLOYER_FULL_RATE = 0.076;

    // Income Tax Constants
    public static final double CREDIT_POINT_VALUE = 242.0;
    public static final double TAX_BRACKET_1_LIMIT = 7010.0;
    public static final double TAX_BRACKET_1_RATE = 0.10;
    public static final double TAX_BRACKET_2_LIMIT = 10060.0;
    public static final double TAX_BRACKET_2_RATE = 0.14;
    public static final double TAX_BRACKET_3_LIMIT = 16150.0;
    public static final double TAX_BRACKET_3_RATE = 0.20;
    public static final double TAX_BRACKET_4_LIMIT = 22440.0;
    public static final double TAX_BRACKET_4_RATE = 0.31;
    public static final double TAX_BRACKET_5_LIMIT = 46690.0;
    public static final double TAX_BRACKET_5_RATE = 0.35;
    public static final double TAX_BRACKET_6_LIMIT = 60130.0;
    public static final double TAX_BRACKET_6_RATE = 0.47;
    // Anything above Bracket 6 is taxed at 50% (including Surtax/Mas Yesef)
    public static final double TAX_BRACKET_7_RATE = 0.50;

    // Overtime Constants
    public static final double REGULAR_DAILY_HOURS_LIMIT = 8.0;
    public static final double OVERTIME_125_LIMIT = 2.0;
    public static final double RATE_125 = 1.25;
    public static final double RATE_150 = 1.50;
}

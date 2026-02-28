-- Reset database: Deletes all data and restarts ID sequences from 1
TRUNCATE TABLE users,archived_employees,employees,events,salary_details,activity_logs,notifications,attendance,departments,salary RESTART IDENTITY CASCADE;

-- Insert default admin user with encrypted password(1234) and company details.
-- 'ON CONFLICT' ensures we don't overwrite or crash if the admin already exists.
insert into users (id,username,email,password,role,enabled,company_name,company_id,company_address,phone_number,last_login,created_at,updated_at)
values (1,'admin','admin@gmail.com',
        '$2a$10$UyowBSgM7yzmle6LC2PlCunTKExE5l0V0TUGNTvOIVlmrFrMqejhe',
        'admin',true,'חברה בע"מ','511111111',
        'רחוב אחד','0711111111',
        localtimestamp,localtimestamp,localtimestamp)
on conflict(id) do nothing;

-- Insert departments linked to admin user (Id:1)
insert into departments (id, date_of_create, annual_placement, department_name, department_code, department_manager, department_phone, department_mail, user_id)
values (1,localtimestamp,1500000,'DEV','DEP_01','מנהל אחד','0111111111','dep01@gmail.com',1);
insert into departments (id, date_of_create, annual_placement, department_name, department_code, department_manager, department_phone, department_mail, user_id)
values (2,localtimestamp,120000,'HR','DEP_02','מנהל שתיים','0211111111','dep02@gmail.com',1);
insert into departments (id, date_of_create, annual_placement, department_name, department_code, department_manager, department_phone, department_mail, user_id)
values (3,localtimestamp,1900000,'SALES','DEP_03','מנהל שלוש','0311111111','dep03@gmail.com',1);
insert into departments (id, date_of_create, annual_placement, department_name, department_code, department_manager, department_phone, department_mail, user_id)
values (4,localtimestamp,15000000,'MARKETING','DEP_04','מנהל ארבע','0411111111','dep04@gmail.com',1);
insert into departments (id, date_of_create, annual_placement, department_name, department_code, department_manager, department_phone, department_mail, user_id)
values (5,localtimestamp,1670000,'IT','DEP_05','מנהל חמש','0511111111','dep05@gmail.com',1);
insert into departments (id, date_of_create, annual_placement, department_name, department_code, department_manager, department_phone, department_mail, user_id)
values (6,localtimestamp,1598000,'MANAGEMENT','DEP_06','מנהל שש','0611111111','dep06@gmail.com',1);
insert into departments (id, date_of_create, annual_placement, department_name, department_code, department_manager, department_phone, department_mail, user_id)
values (7,localtimestamp,1675400,'FINANCE','DEP_07','מנהל שבע','0711111111','dep07@gmail.com',1);

--Insert employees linked to user admin and departments
INSERT INTO employees (first_name, last_name, personal_id, gender, birth_date, family_status, email, phone_number, address, city, country, position, hire_date, job_type, status, status_attendance, user_id, department_id)
VALUES
-- Department 1: DEV
('עובד', 'אחת', '111111111', 'M', '1990-01-01', 'SINGLE', 'emp1@gmail.com', '0500000001', 'רחוב 1', 'עיר', 'ישראל', 'צוות', '2023-01-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 1),
('עובד', 'שתיים', '222222222', 'F', '1990-01-01', 'SINGLE', 'emp2@gmail.com', '0500000002', 'רחוב 2', 'עיר', 'ישראל', 'צוות', '2023-01-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 1),
('עובד', 'שלוש', '333333333', 'M', '1990-01-01', 'SINGLE', 'emp3@gmail.com', '0500000003', 'רחוב 3', 'עיר', 'ישראל', 'צוות', '2023-01-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 1),
('עובד', 'ארבע', '444444444', 'F', '1990-01-01', 'SINGLE', 'emp4@gmail.com', '0500000004', 'רחוב 4', 'עיר', 'ישראל', 'צוות', '2023-01-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 1),
('עובד', 'חמש', '555555555', 'M', '1990-01-01', 'SINGLE', 'emp5@gmail.com', '0500000005', 'רחוב 5', 'עיר', 'ישראל', 'צוות', '2023-01-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 1),
('עובד', 'שש', '666666666', 'F', '1990-01-01', 'SINGLE', 'emp6@gmail.com', '0500000006', 'רחוב 6', 'עיר', 'ישראל', 'צוות', '2023-01-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 1),
('עובד', 'שבע', '777777777', 'M', '1990-01-01', 'SINGLE', 'emp7@gmail.com', '0500000007', 'רחוב 7', 'עיר', 'ישראל', 'צוות', '2023-01-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 1),

-- Department 2: HR
('עובד', 'שמונה', '100000008', 'F', '1992-05-10', 'SINGLE', 'emp8@gmail.com', '0500000008', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-02-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 2),
('עובד', 'תשע', '100000009', 'M', '1992-05-10', 'SINGLE', 'emp9@gmail.com', '0500000009', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-02-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 2),
('עובד', 'עשר', '100000010', 'F', '1992-05-10', 'SINGLE', 'emp10@gmail.com', '0500000010', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-02-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 2),
('עובד', 'אחת עשרה', '100000011', 'M', '1992-05-10', 'SINGLE', 'emp11@gmail.com', '0500000011', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-02-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 2),
('עובד', 'שתים עשרה', '100000012', 'F', '1992-05-10', 'SINGLE', 'emp12@gmail.com', '0500000012', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-02-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 2),
('עובד', 'שלוש עשרה', '100000013', 'M', '1992-05-10', 'SINGLE', 'emp13@gmail.com', '0500000013', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-02-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 2),
('עובד', 'ארבע עשרה', '100000014', 'F', '1992-05-10', 'SINGLE', 'emp14@gmail.com', '0500000014', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-02-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 2),

-- Department 3: SALES
('עובד', 'חמש עשרה', '100000015', 'M', '1988-11-20', 'MARRIED', 'emp15@gmail.com', '0500000015', 'כתובת', 'עיר', 'ישראל', 'צוות', '2022-10-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 3),
('עובד', 'שש עשרה', '100000016', 'F', '1988-11-20', 'MARRIED', 'emp16@gmail.com', '0500000016', 'כתובת', 'עיר', 'ישראל', 'צוות', '2022-10-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 3),
('עובד', 'שבע עשרה', '100000017', 'M', '1988-11-20', 'MARRIED', 'emp17@gmail.com', '0500000017', 'כתובת', 'עיר', 'ישראל', 'צוות', '2022-10-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 3),
('עובד', 'שמונה עשרה', '100000018', 'F', '1988-11-20', 'MARRIED', 'emp18@gmail.com', '0500000018', 'כתובת', 'עיר', 'ישראל', 'צוות', '2022-10-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 3),
('עובד', 'תשע עשרה', '100000019', 'M', '1988-11-20', 'MARRIED', 'emp19@gmail.com', '0500000019', 'כתובת', 'עיר', 'ישראל', 'צוות', '2022-10-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 3),
('עובד', 'עשרים', '100000020', 'F', '1988-11-20', 'MARRIED', 'emp20@gmail.com', '0500000020', 'כתובת', 'עיר', 'ישראל', 'צוות', '2022-10-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 3),
('עובד', 'עשרים ואחת', '100000021', 'M', '1988-11-20', 'MARRIED', 'emp21@gmail.com', '0500000021', 'כתובת', 'עיר', 'ישראל', 'צוות', '2022-10-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 3),

-- Department 4: MARKETING
('עובד', 'עשרים ושתיים', '100000022', 'F', '1995-03-15', 'SINGLE', 'emp22@gmail.com', '0500000022', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-03-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 4),
('עובד', 'עשרים ושלוש', '100000023', 'M', '1995-03-15', 'SINGLE', 'emp23@gmail.com', '0500000023', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-03-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 4),
('עובד', 'עשרים וארבע', '100000024', 'F', '1995-03-15', 'SINGLE', 'emp24@gmail.com', '0500000024', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-03-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 4),
('עובד', 'עשרים וחמש', '100000025', 'M', '1995-03-15', 'SINGLE', 'emp25@gmail.com', '0500000025', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-03-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 4),
('עובד', 'עשרים ושש', '100000026', 'F', '1995-03-15', 'SINGLE', 'emp26@gmail.com', '0500000026', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-03-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 4),
('עובד', 'עשרים ושבע', '100000027', 'M', '1995-03-15', 'SINGLE', 'emp27@gmail.com', '0500000027', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-03-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 4),
('עובד', 'עשרים ושמונה', '100000028', 'F', '1995-03-15', 'SINGLE', 'emp28@gmail.com', '0500000028', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-03-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 4),

-- Department 5: IT
('עובד', 'עשרים ותשע', '100000029', 'M', '1991-07-22', 'SINGLE', 'emp29@gmail.com', '0500000029', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-05-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 5),
('עובד', 'שלושים', '100000030', 'F', '1991-07-22', 'SINGLE', 'emp30@gmail.com', '0500000030', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-05-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 5),
('עובד', 'שלושים ואחת', '100000031', 'M', '1991-07-22', 'SINGLE', 'emp31@gmail.com', '0500000031', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-05-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 5),
('עובד', 'שלושים ושתיים', '100000032', 'F', '1991-07-22', 'SINGLE', 'emp32@gmail.com', '0500000032', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-05-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 5),
('עובד', 'שלושים ושלוש', '100000033', 'M', '1991-07-22', 'SINGLE', 'emp33@gmail.com', '0500000033', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-05-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 5),
('עובד', 'שלושים וארבע', '100000034', 'F', '1991-07-22', 'SINGLE', 'emp34@gmail.com', '0500000034', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-05-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 5),
('עובד', 'שלושים וחמש', '100000035', 'M', '1991-07-22', 'SINGLE', 'emp35@gmail.com', '0500000035', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-05-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 5),

-- Department 6: MANAGEMENT
('עובד', 'שלושים ושש', '100000036', 'F', '1985-01-01', 'MARRIED', 'emp36@gmail.com', '0500000036', 'כתובת', 'עיר', 'ישראל', 'צוות', '2020-01-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 6),
('עובד', 'שלושים ושבע', '100000037', 'M', '1985-01-01', 'MARRIED', 'emp37@gmail.com', '0500000037', 'כתובת', 'עיר', 'ישראל', 'צוות', '2020-01-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 6),
('עובד', 'שלושים ושמונה', '100000038', 'F', '1985-01-01', 'MARRIED', 'emp38@gmail.com', '0500000038', 'כתובת', 'עיר', 'ישראל', 'צוות', '2020-01-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 6),
('עובד', 'שלושים ותשע', '100000039', 'M', '1985-01-01', 'MARRIED', 'emp39@gmail.com', '0500000039', 'כתובת', 'עיר', 'ישראל', 'צוות', '2020-01-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 6),
('עובד', 'ארבעים', '100000040', 'F', '1985-01-01', 'MARRIED', 'emp40@gmail.com', '0500000040', 'כתובת', 'עיר', 'ישראל', 'צוות', '2020-01-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 6),
('עובד', 'ארבעים ואחת', '100000041', 'M', '1985-01-01', 'MARRIED', 'emp41@gmail.com', '0500000041', 'כתובת', 'עיר', 'ישראל', 'צוות', '2020-01-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 6),
('עובד', 'ארבעים ושתיים', '100000042', 'F', '1985-01-01', 'MARRIED', 'emp42@gmail.com', '0500000042', 'כתובת', 'עיר', 'ישראל', 'צוות', '2020-01-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 6),

-- Department 7: FINANCE
('עובד', 'ארבעים ושלוש', '100000043', 'M', '1990-01-01', 'SINGLE', 'emp43@gmail.com', '0500000043', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-01-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 7),
('עובד', 'ארבעים וארבע', '100000044', 'F', '1990-01-01', 'SINGLE', 'emp44@gmail.com', '0500000044', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-01-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 7),
('עובד', 'ארבעים וחמש', '100000045', 'M', '1990-01-01', 'SINGLE', 'emp45@gmail.com', '0500000045', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-01-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 7),
('עובד', 'ארבעים ושש', '100000046', 'F', '1990-01-01', 'SINGLE', 'emp46@gmail.com', '0500000046', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-01-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 7),
('עובד', 'ארבעים ושבע', '100000047', 'M', '1990-01-01', 'SINGLE', 'emp47@gmail.com', '0500000047', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-01-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 7),
('עובד', 'ארבעים ושמונה', '100000048', 'F', '1990-01-01', 'SINGLE', 'emp48@gmail.com', '0500000048', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-01-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 7),
('עובד', 'ארבעים ותשע', '100000049', 'M', '1990-01-01', 'SINGLE', 'emp49@gmail.com', '0500000049', 'כתובת', 'עיר', 'ישראל', 'צוות', '2023-01-01', 'FULL-TIME', 'ACTIVE', 'PRESENT', 1, 7)

ON CONFLICT (personal_id) DO NOTHING;

--Insert salary details
INSERT INTO salary_details (
    employee_id,
    pension_fund,
    provident_fund,
    insurance_company,
    total_seek_days,
    total_vacation_days,
    salary_per_hour,
    seniority,
    credit_points
)
SELECT
    id,
    'קרן פנסיה א',
    'קופת גמל ב',
    'חברת ביטוח ג',
    10,
    15,
    CASE
        WHEN department_id = 6 THEN 150.0
        WHEN department_id = 1 THEN 85.0
        ELSE 55.0
        END,
    EXTRACT(YEAR FROM AGE(NOW(), hire_date)),
    2.25
FROM employees;

--Script for Generating Automated Attendance Records (2024-2026)
INSERT INTO attendance (date, check_in_time, check_out_time, total_hours, status, travel_alow, notes, is_attendance_closed, employee_id)
SELECT
    day_series.date,
    CASE
        WHEN s.current_status = 'PRESENT' THEN date_trunc('minute', (TIME '08:00:00' + (random() * INTERVAL '90 minutes')))::TIME(0)
        END as check_in,
    CASE
        WHEN s.current_status = 'PRESENT' THEN date_trunc('minute', (TIME '16:30:00' + (random() * INTERVAL '120 minutes')))::TIME(0)
        END as check_out,
    CASE
        WHEN s.current_status = 'PRESENT' THEN ROUND((8.5 + (random() * 1.5))::numeric, 2)
        ELSE 0
        END as hours,
    s.current_status,
    (s.current_status = 'PRESENT' AND random() < 0.4) as travel_alow,
    CASE
        WHEN s.current_status = 'SICK' THEN 'אישור מחלה'
        WHEN s.current_status = 'VACATION' THEN 'חופשה שנתית'
        WHEN s.current_status = 'PRESENT' AND random() < 0.05 THEN 'עבודה מרחוק'
        END,
    FALSE as is_attendance_closed,
    e.id
FROM
    employees e
        CROSS JOIN
    generate_series('2024-01-01'::date, '2026-12-31'::date, '1 day'::interval) AS day_series(date)
        CROSS JOIN LATERAL (
        SELECT CASE
                   WHEN random() < 0.85 THEN 'PRESENT'
                   WHEN random() < 0.50 THEN 'SICK'
                   ELSE 'VACATION'
                   END as current_status
        ) s
WHERE
    EXTRACT(ISODOW FROM day_series.date) NOT IN (5, 6);


--Script for Generating Events (Upcoming and Past)
INSERT INTO events (
    id,
    event_name,
    event_date,
    event_time,
    priority,
    description,
    location,
    number_of_attendance,
    user_id
)
VALUES
(1,'ישיבת צוות פיתוח - Sprint Planning', CURRENT_DATE + INTERVAL '1 day', '10:00:00', 'HIGH', 'תכנון משימות לשבועיים הקרובים וסקירת באגים קריטיים.', 'חדר ישיבות קומה 2', 12, 1),
(2,'Happy Hour - הרמת כוסית', CURRENT_DATE + INTERVAL '3 days', '16:00:00', 'LOW', 'כיבוד ושתייה בלובי המשרד לכל עובדי החברה.', 'לובי מרכזי', 49, 1),
(3,'הדרכת אבטחת מידע שנתית', CURRENT_DATE + INTERVAL '5 days', '09:00:00', 'MEDIUM', 'חובת השתתפות לכל העובדים - ריענון נהלי סייבר.', 'אולם הרצאות', 25, 1),

(4,'ראיון עבודה - מפתח Fullstack', CURRENT_DATE - INTERVAL '2 days', '11:30:00', 'MEDIUM', 'ראיון מקצועי למועמד חדש עבור צוות התשתיות.', 'חדר ראיונות 1', 2, 1),
(5,'ישיבת הנהלה רבעונית', CURRENT_DATE - INTERVAL '10 days', '14:00:00', 'HIGH', 'סקירת ביצועים כספיים לרבעון הקודם ואישור תקציבים.', 'חדר דירקטוריון', 8, 1),
(6,'סדנת ניהול זמן', CURRENT_DATE - INTERVAL '20 days', '09:00:00', 'LOW', 'סדנה מעשית לשיפור פריון העבודה בקרב מנהלי צוותים.', 'חדר הדרכה', 15, 1);
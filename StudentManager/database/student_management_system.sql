
-- ============================================================
-- DATABASE: STUDENT_MANAGEMENT
-- MYSQL VERSION: 8.0.16+
-- PROJECT: HE THONG QUAN LY SINH VIEN VA DANG KY HOC PHAN
-- ============================================================


-- ============================================================
-- 1. TAO DATABASE
-- ============================================================

DROP DATABASE IF EXISTS student_management;

CREATE DATABASE student_management
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE student_management;


-- ============================================================
-- 2. BANG STUDENTS: QUAN LY SINH VIEN
-- ============================================================

CREATE TABLE students (
    student_id VARCHAR(20) PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    birth_date DATE,
    gender VARCHAR(10),
    email VARCHAR(100),
    phone VARCHAR(20),
    address VARCHAR(255),
    major VARCHAR(100),
    class_name VARCHAR(50),
    gpa DOUBLE DEFAULT 0,

    CONSTRAINT chk_students_gpa
    CHECK (gpa IS NULL OR gpa BETWEEN 0 AND 4)
);


-- ============================================================
-- 3. BANG COURSES: QUAN LY MON HOC
-- ============================================================

CREATE TABLE courses (
    course_id VARCHAR(20) PRIMARY KEY,
    course_name VARCHAR(100) NOT NULL,
    credits INT NOT NULL,
    description VARCHAR(255),

    CONSTRAINT chk_courses_credits
    CHECK (credits > 0)
);


-- ============================================================
-- 4. BANG COURSE_CLASSES: QUAN LY LOP HOC PHAN
-- ============================================================

CREATE TABLE course_classes (
    class_id INT AUTO_INCREMENT PRIMARY KEY,
    course_id VARCHAR(20) NOT NULL,
    lecturer VARCHAR(100) NOT NULL,
    semester VARCHAR(50) NOT NULL,
    academic_year VARCHAR(20) NOT NULL,
    max_capacity INT DEFAULT 40,
    enrolled INT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'OPEN',

    CONSTRAINT fk_cc_course
    FOREIGN KEY (course_id)
    REFERENCES courses(course_id)
    ON DELETE CASCADE,

    CONSTRAINT chk_cc_capacity
    CHECK (max_capacity > 0),

    CONSTRAINT chk_cc_enrolled
    CHECK (enrolled >= 0 AND enrolled <= max_capacity),

    CONSTRAINT chk_cc_status
    CHECK (status IN ('OPEN', 'CLOSED'))
);


-- ============================================================
-- 5. BANG REGISTRATIONS: DANG KY HOC PHAN VA QUAN LY DIEM
-- ============================================================

CREATE TABLE registrations (
    registration_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL,
    class_id INT NOT NULL,
    registration_date DATE NOT NULL,

    midterm_score DOUBLE NULL,
    final_score DOUBLE NULL,
    score DOUBLE NULL,

    CONSTRAINT uq_student_class
    UNIQUE (student_id, class_id),

    CONSTRAINT fk_reg_student
    FOREIGN KEY (student_id)
    REFERENCES students(student_id)
    ON DELETE CASCADE,

    CONSTRAINT fk_reg_class
    FOREIGN KEY (class_id)
    REFERENCES course_classes(class_id)
    ON DELETE CASCADE,

    CONSTRAINT chk_reg_midterm
    CHECK (
        midterm_score IS NULL
        OR midterm_score BETWEEN 0 AND 10
    ),

    CONSTRAINT chk_reg_final
    CHECK (
        final_score IS NULL
        OR final_score BETWEEN 0 AND 10
    ),

    CONSTRAINT chk_reg_score
    CHECK (
        score IS NULL
        OR score BETWEEN 0 AND 10
    )
);

CREATE INDEX idx_reg_class
ON registrations(class_id);

CREATE INDEX idx_reg_student
ON registrations(student_id);


-- ============================================================
-- 6. BANG USERS: QUAN LY TAI KHOAN
-- ============================================================

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'STUDENT') NOT NULL,
    student_id VARCHAR(20) UNIQUE,

    CONSTRAINT fk_users_student
    FOREIGN KEY (student_id)
    REFERENCES students(student_id)
    ON DELETE CASCADE
);


-- ============================================================
-- 7. BANG SCHEDULES: QUAN LY THOI KHOA BIEU
-- ============================================================

CREATE TABLE schedules (
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    class_id INT NOT NULL,
    day_of_week VARCHAR(20) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    room VARCHAR(50) NOT NULL,

    CONSTRAINT fk_sched_class
    FOREIGN KEY (class_id)
    REFERENCES course_classes(class_id)
    ON DELETE CASCADE,

    CONSTRAINT chk_sched_time
    CHECK (start_time < end_time)
);

CREATE INDEX idx_sched_room
ON schedules(room, day_of_week);

CREATE INDEX idx_sched_class
ON schedules(class_id);


-- ============================================================
-- 8. TRIGGER: TU DONG TINH DIEM TONG KET
-- ============================================================

DELIMITER $$

CREATE TRIGGER trg_registration_calculate_score_insert
BEFORE INSERT ON registrations
FOR EACH ROW
BEGIN
    IF NEW.midterm_score IS NULL OR NEW.final_score IS NULL THEN
        SET NEW.score = NULL;
    ELSE
        SET NEW.score = ROUND(
            NEW.midterm_score * 0.3 +
            NEW.final_score * 0.7,
            2
        );
    END IF;
END$$


CREATE TRIGGER trg_registration_calculate_score_update
BEFORE UPDATE ON registrations
FOR EACH ROW
BEGIN
    IF NEW.midterm_score IS NULL OR NEW.final_score IS NULL THEN
        SET NEW.score = NULL;
    ELSE
        SET NEW.score = ROUND(
            NEW.midterm_score * 0.3 +
            NEW.final_score * 0.7,
            2
        );
    END IF;
END$$


-- ============================================================
-- 9. TRIGGER: TU DONG CAP NHAT GPA
-- ============================================================

CREATE TRIGGER trg_registration_gpa_insert
AFTER INSERT ON registrations
FOR EACH ROW
BEGIN
    UPDATE students s
    SET s.gpa = COALESCE(
        (
            SELECT ROUND(
                SUM(r.score * 0.4 * c.credits)
                / NULLIF(SUM(c.credits), 0),
                2
            )
            FROM registrations r
            JOIN course_classes cc
                ON r.class_id = cc.class_id
            JOIN courses c
                ON cc.course_id = c.course_id
            WHERE r.student_id = NEW.student_id
              AND r.score IS NOT NULL
        ),
        0
    )
    WHERE s.student_id = NEW.student_id;
END$$


CREATE TRIGGER trg_registration_gpa_update
AFTER UPDATE ON registrations
FOR EACH ROW
BEGIN
    UPDATE students s
    SET s.gpa = COALESCE(
        (
            SELECT ROUND(
                SUM(r.score * 0.4 * c.credits)
                / NULLIF(SUM(c.credits), 0),
                2
            )
            FROM registrations r
            JOIN course_classes cc
                ON r.class_id = cc.class_id
            JOIN courses c
                ON cc.course_id = c.course_id
            WHERE r.student_id = NEW.student_id
              AND r.score IS NOT NULL
        ),
        0
    )
    WHERE s.student_id = NEW.student_id;

    IF OLD.student_id <> NEW.student_id THEN
        UPDATE students s
        SET s.gpa = COALESCE(
            (
                SELECT ROUND(
                    SUM(r.score * 0.4 * c.credits)
                    / NULLIF(SUM(c.credits), 0),
                    2
                )
                FROM registrations r
                JOIN course_classes cc
                    ON r.class_id = cc.class_id
                JOIN courses c
                    ON cc.course_id = c.course_id
                WHERE r.student_id = OLD.student_id
                  AND r.score IS NOT NULL
            ),
            0
        )
        WHERE s.student_id = OLD.student_id;
    END IF;
END$$


CREATE TRIGGER trg_registration_gpa_delete
AFTER DELETE ON registrations
FOR EACH ROW
BEGIN
    UPDATE students s
    SET s.gpa = COALESCE(
        (
            SELECT ROUND(
                SUM(r.score * 0.4 * c.credits)
                / NULLIF(SUM(c.credits), 0),
                2
            )
            FROM registrations r
            JOIN course_classes cc
                ON r.class_id = cc.class_id
            JOIN courses c
                ON cc.course_id = c.course_id
            WHERE r.student_id = OLD.student_id
              AND r.score IS NOT NULL
        ),
        0
    )
    WHERE s.student_id = OLD.student_id;
END$$

DELIMITER ;


-- ============================================================
-- 10. VIEW: BANG DIEM CHI TIET
-- ============================================================

CREATE VIEW v_student_grades AS
SELECT
    r.registration_id,
    r.student_id,
    s.full_name,
    r.class_id,
    c.course_id,
    c.course_name,
    c.credits,
    cc.lecturer,
    cc.semester,
    cc.academic_year,
    r.registration_date,
    r.midterm_score,
    r.final_score,
    r.score,

    CASE
        WHEN r.score IS NULL THEN 'Chua co diem'
        WHEN r.score >= 5 THEN 'Dat'
        ELSE 'Rot'
    END AS result

FROM registrations r

JOIN students s
    ON r.student_id = s.student_id

JOIN course_classes cc
    ON r.class_id = cc.class_id

JOIN courses c
    ON cc.course_id = c.course_id;


-- ============================================================
-- 11. VIEW: TINH GPA THEO TIN CHI
-- ============================================================

CREATE VIEW v_student_gpa AS
SELECT
    r.student_id,

    ROUND(
        SUM(r.score * 0.4 * c.credits)
        / NULLIF(SUM(c.credits), 0),
        2
    ) AS gpa,

    SUM(c.credits) AS total_graded_credits,

    SUM(
        CASE
            WHEN r.score >= 5 THEN c.credits
            ELSE 0
        END
    ) AS passed_credits,

    SUM(
        CASE
            WHEN r.score < 5 THEN c.credits
            ELSE 0
        END
    ) AS failed_credits

FROM registrations r

JOIN course_classes cc
    ON r.class_id = cc.class_id

JOIN courses c
    ON cc.course_id = c.course_id

WHERE r.score IS NOT NULL

GROUP BY r.student_id;


-- ============================================================
-- 12. VIEW: HIEN THI DAY DU THONG TIN SINH VIEN
-- ============================================================

CREATE VIEW v_student_full AS
SELECT
    s.student_id,
    s.full_name,
    s.birth_date,
    s.gender,
    s.email,
    s.phone,
    s.address,
    s.major,
    s.class_name,

    COALESCE(g.gpa, 0) AS gpa,

    COALESCE(g.total_graded_credits, 0)
        AS total_graded_credits,

    COALESCE(g.passed_credits, 0)
        AS passed_credits,

    COALESCE(g.failed_credits, 0)
        AS failed_credits

FROM students s

LEFT JOIN v_student_gpa g
    ON s.student_id = g.student_id;


-- ============================================================
-- 13. VIEW: HIEN THI DAY DU THONG TIN DANG KY
-- ============================================================

CREATE VIEW v_registration_full AS
SELECT
    r.registration_id,
    r.student_id,
    s.full_name,
    r.class_id,
    c.course_id,
    c.course_name,
    c.credits,
    cc.lecturer,
    cc.semester,
    cc.academic_year,
    r.registration_date,
    r.midterm_score,
    r.final_score,
    r.score,

    CASE
        WHEN r.score IS NULL THEN 'Chua co diem'
        WHEN r.score >= 5 THEN 'Dat'
        ELSE 'Rot'
    END AS result

FROM registrations r

JOIN students s
    ON r.student_id = s.student_id

JOIN course_classes cc
    ON r.class_id = cc.class_id

JOIN courses c
    ON cc.course_id = c.course_id;


-- ============================================================
-- 14. DU LIEU MAU: SINH VIEN
-- ============================================================

INSERT INTO students
(student_id, full_name, birth_date, gender, email,
 phone, address, major, class_name, gpa)
VALUES
('SV001', 'Nguyen Van A', '2003-05-10', 'Nam',
 'a@gmail.com', '0909123456', 'TP HCM',
 'CNTT', 'DHKTPM17A', 0),

('SV002', 'Tran Thi B', '2004-08-15', 'Nu',
 'b@gmail.com', '0909234567', 'Ha Noi',
 'CNTT', 'DHKTPM17A', 0),

('SV003', 'Le Van C', '2003-12-20', 'Nam',
 'c@gmail.com', '0909345678', 'Da Nang',
 'KTPM', 'DHKTPM17B', 0);


-- ============================================================
-- 15. DU LIEU MAU: MON HOC
-- ============================================================

INSERT INTO courses
(course_id, course_name, credits, description)
VALUES
('MH001', 'Co so du lieu', 3, 'Hoc SQL va DBMS'),
('MH002', 'Lap trinh Java', 4, 'Java Core va Swing'),
('MH003', 'Cau truc du lieu', 3, 'DSA');


-- ============================================================
-- 16. DU LIEU MAU: LOP HOC PHAN
-- ============================================================

INSERT INTO course_classes
(class_id, course_id, lecturer, semester,
 academic_year, max_capacity, enrolled, status)
VALUES
(1, 'MH001', 'Thay Nam', 'Hoc ky 1',
 '2025-2026', 40, 2, 'OPEN'),

(2, 'MH002', 'Thay Binh', 'Hoc ky 1',
 '2025-2026', 35, 1, 'OPEN'),

(3, 'MH003', 'Co Lan', 'Hoc ky 2',
 '2025-2026', 40, 0, 'OPEN');


-- ============================================================
-- 17. DU LIEU MAU: DANG KY VA DIEM
-- Trigger tu dong tinh score
-- ============================================================

INSERT INTO registrations
(student_id, class_id, registration_date,
 midterm_score, final_score)
VALUES
('SV001', 1, '2025-08-20', 8.0, 7.5),
('SV002', 1, '2025-08-20', 6.0, 4.0),
('SV001', 2, '2025-08-21', 9.0, 8.0);


-- ============================================================
-- 18. DU LIEU MAU: TAI KHOAN
-- ============================================================

INSERT INTO users
(username, password, role, student_id)
VALUES
('admin', '123456', 'ADMIN', NULL),
('SV001', '123456', 'STUDENT', 'SV001'),
('SV002', '123456', 'STUDENT', 'SV002'),
('SV003', '123456', 'STUDENT', 'SV003');


-- ============================================================
-- 19. DU LIEU MAU: THOI KHOA BIEU
-- ============================================================

INSERT INTO schedules
(class_id, day_of_week, start_time, end_time, room)
VALUES
(1, 'Thu 2', '07:00:00', '09:30:00', 'A101'),
(2, 'Thu 4', '09:45:00', '12:15:00', 'B202'),
(3, 'Thu 6', '13:00:00', '15:30:00', 'C303');


-- ============================================================
-- 20. KIEM TRA DATABASE
-- ============================================================

-- Danh sach bang va VIEW
SHOW FULL TABLES;

-- Thong tin sinh vien va GPA
SELECT * FROM v_student_full;

-- Bang sinh vien goc
SELECT * FROM students;

-- Danh sach mon hoc
SELECT * FROM courses;

-- Danh sach lop hoc phan
SELECT * FROM course_classes;

-- Danh sach dang ky
SELECT * FROM registrations;

-- Bang diem chi tiet
SELECT * FROM v_student_grades;

-- Bang diem day du
SELECT * FROM v_registration_full;

-- Thong ke GPA va tin chi
SELECT * FROM v_student_gpa;

-- Tai khoan
SELECT * FROM users;

-- Thoi khoa bieu
SELECT * FROM schedules;
SELECT
    registration_id,
    student_id,
    midterm_score,
    final_score,
    score
FROM registrations;

SELECT
    student_id,
    full_name,
    gpa
FROM students;
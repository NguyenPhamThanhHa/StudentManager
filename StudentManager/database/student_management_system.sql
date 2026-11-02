-- 1. Tạo và sử dụng cơ sở dữ liệu student_management
DROP DATABASE IF EXISTS student_management;
CREATE DATABASE student_management;
USE student_management;

-- 2. Tạo bảng students (Sinh viên)
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
    gpa DOUBLE
);

-- 3. Tạo bảng courses (Môn học / Học phần gốc)
CREATE TABLE courses (
    course_id VARCHAR(20) PRIMARY KEY,
    course_name VARCHAR(100) NOT NULL,
    credits INT NOT NULL,
    description VARCHAR(255)
);

-- 4. Tạo bảng course_classes (Lớp học phần - Bổ sung theo đặc tả quản lý lớp, giảng viên, sĩ số)
CREATE TABLE course_classes (
    class_id INT AUTO_INCREMENT PRIMARY KEY,
    course_id VARCHAR(20) NOT NULL,
    lecturer VARCHAR(100) NOT NULL,
    semester VARCHAR(50) NOT NULL,
    academic_year VARCHAR(20) NOT NULL,
    max_capacity INT DEFAULT 40,
    enrolled INT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'OPEN', -- 'OPEN' hoặc 'CLOSED'
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
);

-- 5. Tạo bảng registrations (Đăng ký học phần theo Lớp học phần)
CREATE TABLE registrations (
    registration_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL,
    class_id INT NOT NULL,
    registration_date DATE NOT NULL,
    UNIQUE (student_id, class_id), -- Một sinh viên không được đăng ký trùng một lớp học phần
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (class_id) REFERENCES course_classes(class_id) ON DELETE CASCADE
);

-- 6. Tạo bảng users (Tài khoản đăng nhập)
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'STUDENT') NOT NULL,
    student_id VARCHAR(20) UNIQUE,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE
);

-- 7. Tạo bảng schedules (Thời khóa biểu gắn với Lớp học phần để kiểm tra trùng lịch phòng/giảng viên)
CREATE TABLE schedules (
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    class_id INT NOT NULL,
    day_of_week VARCHAR(20) NOT NULL, -- Thứ 2, Thứ 3, ...
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    room VARCHAR(50) NOT NULL,
    FOREIGN KEY (class_id) REFERENCES course_classes(class_id) ON DELETE CASCADE
);

-- ==========================================
-- THÊM DỮ LIỆU MẪU (TEST DATA)
-- ==========================================

-- 8. Thêm sinh viên mẫu
INSERT INTO students VALUES
('SV001', 'Nguyen Van A', '2003-05-10', 'Nam', 'a@gmail.com', '0909123456', 'TP HCM', 'CNTT', 'DHKTPM17A', 3.5),
('SV002', 'Tran Thi B', '2004-08-15', 'Nu', 'b@gmail.com', '0909234567', 'Ha Noi', 'CNTT', 'DHKTPM17A', 3.8),
('SV003', 'Le Van C', '2003-12-20', 'Nam', 'c@gmail.com', '0909345678', 'Da Nang', 'KTPM', 'DHKTPM17B', 3.2);

-- 9. Thêm môn học gốc
INSERT INTO courses VALUES
('MH001', 'Co so du lieu', 3, 'Hoc SQL va DBMS'),
('MH002', 'Lap trinh Java', 4, 'Java Core va Swing'),
('MH003', 'Cau truc du lieu', 3, 'DSA');

-- 10. Thêm các lớp học phần mở (Course Classes)
INSERT INTO course_classes (class_id, course_id, lecturer, semester, academic_year, max_capacity, enrolled, status) VALUES
(1, 'MH001', 'Thay Nam', 'Học kỳ 1', '2025-2026', 40, 2, 'OPEN'),
(2, 'MH002', 'Thầy Bình', 'Học kỳ 1', '2025-2026', 35, 1, 'OPEN'),
(3, 'MH003', 'Cô Lan', 'Học kỳ 2', '2025-2026', 40, 0, 'OPEN');

-- 11. Thêm tài khoản đăng nhập
INSERT INTO users (username, password, role, student_id) VALUES
('admin', '123456', 'ADMIN', NULL),
('SV001', '123456', 'STUDENT', 'SV001'),
('SV002', '123456', 'STUDENT', 'SV002'),
('SV003', '123456', 'STUDENT', 'SV003');

-- 12. Thêm lịch học chi tiết cho từng lớp học phần
INSERT INTO schedules (class_id, day_of_week, start_time, end_time, room) VALUES
(1, 'Thứ 2', '07:00:00', '09:30:00', 'A101'),
(2, 'Thứ 4', '09:45:00', '12:15:00', 'B202'),
(3, 'Thứ 6', '13:00:00', '15:30:00', 'C303');
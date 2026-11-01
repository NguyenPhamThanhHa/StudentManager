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

-- 3. Tạo bảng courses (Môn học)
CREATE TABLE courses (
    course_id VARCHAR(20) PRIMARY KEY,
    course_name VARCHAR(100) NOT NULL,
    credits INT NOT NULL,
    description VARCHAR(255),
    lecturer VARCHAR(100),
    semester VARCHAR(50)
);

-- 4. Tạo bảng registrations (Đăng ký học phần)
CREATE TABLE registrations (
    registration_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL,
    course_id VARCHAR(20) NOT NULL,
    registration_date DATE NOT NULL,
    UNIQUE (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
);

-- 5. Tạo bảng users (Tài khoản đăng nhập)
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'STUDENT') NOT NULL,
    student_id VARCHAR(20) UNIQUE,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE
);

-- 6. Tạo bảng schedules (Thời khóa biểu)
CREATE TABLE schedules (
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    course_id VARCHAR(20) NOT NULL,
    day_of_week VARCHAR(20) NOT NULL,
    start_period INT NOT NULL,
    end_period INT NOT NULL,
    room VARCHAR(50),
    study_time VARCHAR(100),
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
);

-- 7. Thêm dữ liệu mẫu cho sinh viên
INSERT INTO students VALUES
('SV001', 'Nguyen Van A', '2003-05-10', 'Nam', 'a@gmail.com', '0909123456', 'TP HCM', 'CNTT', 'DHKTPM17A', 3.5),
('SV002', 'Tran Thi B', '2004-08-15', 'Nu', 'b@gmail.com', '0909234567', 'Ha Noi', 'CNTT', 'DHKTPM17A', 3.8),
('SV003', 'Le Van C', '2003-12-20', 'Nam', 'c@gmail.com', '0909345678', 'Da Nang', 'KTPM', 'DHKTPM17B', 3.2);

-- 8. Thêm dữ liệu mẫu cho môn học
INSERT INTO courses (course_id, course_name, credits, description, lecturer, semester) VALUES
('MH001', 'Co so du lieu', 3, 'Hoc SQL va DBMS', 'Thay Nam', 'Học kỳ 1'),
('MH002', 'Lap trinh Java', 4, 'Java Core va Swing', 'Thầy Bình', 'Học kỳ 1'),
('MH003', 'Cau truc du lieu', 3, 'DSA', 'Cô Lan', 'Học kỳ 2');

-- 9. Thêm tài khoản đăng nhập (Admin và Sinh viên)
INSERT INTO users (username, password, role, student_id) VALUES
('admin', '123456', 'ADMIN', NULL),
('SV001', '123456', 'STUDENT', 'SV001'),
('SV002', '123456', 'STUDENT', 'SV002'),
('SV003', '123456', 'STUDENT', 'SV003');

-- 10. Thêm dữ liệu thời khóa biểu mẫu
INSERT INTO schedules (course_id, day_of_week, start_period, end_period, room, study_time) VALUES
('MH001', 'Thứ 2', 1, 3, 'A101', '07:00 - 09:30'),
('MH002', 'Thứ 4', 4, 6, 'B202', '09:45 - 12:15'),
('MH003', 'Thứ 6', 7, 9, 'C303', '13:00 - 15:30');
SELECT * FROM users;
SELECT
    user_id,
    username,
    password,
    role,
    student_id
FROM users
WHERE username = 'admin'
AND password = '123456';
SELECT @@hostname, @@port, CURRENT_USER();
SELECT 1;
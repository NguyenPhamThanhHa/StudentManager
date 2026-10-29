CREATE DATABASE IF NOT EXISTS student_management;
USE student_management;

-- Xóa bảng cũ nếu có (đảm bảo thứ tự xóa để tránh lỗi khóa ngoại)
DROP TABLE IF EXISTS registrations;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS students;

-- 1. Bảng sinh viên
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

-- 2. Bảng môn học
CREATE TABLE courses (
    course_id VARCHAR(20) PRIMARY KEY,
    course_name VARCHAR(100) NOT NULL,
    credits INT NOT NULL,
    description VARCHAR(255)
);

-- 3. Bảng đăng ký học phần (Bảng trung gian)
CREATE TABLE registrations (
    registration_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL,
    course_id VARCHAR(20) NOT NULL,
    registration_date DATE NOT NULL,
    UNIQUE (student_id, course_id), -- Chống đăng ký trùng lặp một môn nhiều lần
    FOREIGN KEY (student_id) REFERENCES students(student_id)
        ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(course_id)
        ON DELETE CASCADE
);

-- 4. Dữ liệu mẫu sinh viên
INSERT INTO students VALUES
('SV001','Nguyen Van A','2003-05-10','Nam','a@gmail.com','0909123456','TP HCM','CNTT','DHKTPM17A',3.5),
('SV002','Tran Thi B','2004-08-15','Nu','b@gmail.com','0909234567','Ha Noi','CNTT','DHKTPM17A',3.8),
('SV003','Le Van C','2003-12-20','Nam','c@gmail.com','0909345678','Da Nang','KTPM','DHKTPM17B',3.2);

-- 5. Dữ liệu mẫu môn học
INSERT INTO courses VALUES
('MH001','Co so du lieu',3,'Hoc SQL va DBMS'),
('MH002','Lap trinh Java',4,'Java Core va Swing'),
('MH003','Cau truc du lieu',3,'DSA');

-- 6. Dữ liệu mẫu đăng ký
INSERT INTO registrations(student_id, course_id, registration_date)
VALUES
('SV001','MH001','2025-01-10'),
('SV001','MH002','2025-01-10'),
('SV002','MH001','2025-01-11'),
('SV003','MH003','2025-01-12');

-- Kiểm tra kết quả
SHOW TABLES;
SELECT * FROM students;
SELECT * FROM courses;
SELECT * FROM registrations;
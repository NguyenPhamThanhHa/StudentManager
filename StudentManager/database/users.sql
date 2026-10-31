CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'STUDENT') NOT NULL,
    student_id VARCHAR(20) UNIQUE,
    FOREIGN KEY (student_id)
        REFERENCES students(student_id)
        ON DELETE CASCADE
);

-- Thêm tài khoản mẫu
INSERT INTO users (username, password, role, student_id)
VALUES
('admin', '123456', 'ADMIN', NULL),
('SV001', '123456', 'STUDENT', 'SV001'),
('SV002', '123456', 'STUDENT', 'SV002'),
('SV003', '123456', 'STUDENT', 'SV003');

-- Hiển thị bảng users
SELECT * FROM users;
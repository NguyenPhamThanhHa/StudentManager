CREATE TABLE grades (
    grade_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL,
    course_id VARCHAR(20) NOT NULL,
    attendance_score DOUBLE,
    midterm_score DOUBLE,
    final_score DOUBLE,
    total_score DOUBLE,
    result VARCHAR(30),
    UNIQUE (student_id, course_id),
    FOREIGN KEY (student_id)
        REFERENCES students(student_id)
        ON DELETE CASCADE,
    FOREIGN KEY (course_id)
        REFERENCES courses(course_id)
        ON DELETE CASCADE
);

-- Thêm điểm mẫu
INSERT INTO grades
(student_id, course_id, attendance_score, midterm_score,
 final_score, total_score, result)
VALUES
('SV001', 'MH001', 9, 8, 8, 8.2, 'Đạt'),
('SV001', 'MH002', 8, 7, 9, 8.2, 'Đạt'),
('SV002', 'MH001', 9, 9, 9, 9, 'Đạt'),
('SV003', 'MH003', 7, 6, 5, 5.8, 'Đạt');

-- Hiển thị bảng grades
SELECT * FROM grades;
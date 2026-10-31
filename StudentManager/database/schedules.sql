CREATE TABLE schedules (
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    course_id VARCHAR(20) NOT NULL,
    day_of_week VARCHAR(20) NOT NULL,
    start_period INT NOT NULL,
    end_period INT NOT NULL,
    room VARCHAR(50),
    study_time VARCHAR(100),
    FOREIGN KEY (course_id)
        REFERENCES courses(course_id)
        ON DELETE CASCADE
);

-- Thêm thời khóa biểu mẫu
INSERT INTO schedules
(course_id, day_of_week, start_period, end_period, room, study_time)
VALUES
('MH001', 'Thứ 2', 1, 3, 'A101', '07:00 - 09:30'),
('MH002', 'Thứ 4', 4, 6, 'B202', '09:45 - 12:15'),
('MH003', 'Thứ 6', 7, 9, 'C303', '13:00 - 15:30');

-- Hiển thị bảng schedules
SELECT * FROM schedules;
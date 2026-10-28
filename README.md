# STUDENT MANAGEMENT SYSTEM AND COURSE REGISTRATION

# 1. Introduction

The Student Management System and Course Registration is a Java application designed to manage student information, courses, and course registration processes. The system helps simplify administrative tasks in educational institutions while ensuring accuracy, convenience, and efficiency in course registration.

The application features a graphical user interface built with Java Swing, uses Java Collections for in-memory data processing, and connects to a MySQL database through JDBC for data storage and retrieval. In addition, the system incorporates exception handling and file handling to improve reliability and support data backup and export.

# 2. Main Features

# Student Management

- Add new students
- Update student information (date of birth, student ID, address, and phone number)
- Delete student records
- Search for students
- Display the list of students
- Validate input data before adding or updating records
- Prevent duplicate student IDs

# Course Management

- Add new courses
- Update course information, including course code, course name, credit hours, and instructor
- Delete courses when the relevant conditions are satisfied
- Search for courses by course code or course name
- Display the list of available courses
- Validate course information
- Prevent duplicate course codes

# Course Registration Management

- Allow students to register for courses
- Display the courses registered by each student
- Cancel course registrations
- Retrieve the list of students registered for a specific course
- Verify that both students and courses exist before registration
- Prevent duplicate course registrations
- Validate the number of registered courses or total credit hours according to system-defined limits
- Display course registration details for each student

# Statistics and Reports

- Display the total number of students
- Display the total number of courses
- Count the number of students registered for each course
- Calculate the total registered credit hours for each student
- Retrieve the list of students enrolled in a specific course
- Display statistical reports through the graphical user interface

# Data Management and File Handling

- Export student records to CSV or TXT files
- Export course records to CSV or TXT files
- Export course registration records to files
- Import data from files for initial data entry
- Display notifications when file operations are completed successfully
- Handle missing files, access permission errors, and invalid file data

# User Interface

- Build the graphical user interface using Java Swing
- Display data using JTable
- Use JTextField for data entry
- Use JButton to perform operations such as adding, updating, deleting, searching, and registering for courses
- Use JComboBox to select courses and predefined values
- Use JOptionPane to display notifications, warnings, and confirmation dialogs
- Organize the interface into student management, course management, course registration, and statistics sections

# 3. Technologies Used

**- Programming Language:** Java

**- GUI:** Java Swing

**- Database:** MySQL

**- Database Connectivity:** JDBC

**- Core Concepts:** Object-Oriented Programming (OOP), Java Collections Framework

**- Additional Techniques:** File Handling and Exception Handling

# 4. Database Design

# The system is designed around three main entities:

**- Student:** Stores student information.

**- Course:** Stores course information.

**- Enrollment:** Stores course registration records and establishes relationships between students and courses.

Primary keys and foreign keys are used to maintain data integrity and relationships between database records.

=======================================================

# HỆ THỐNG QUẢN LÝ SINH VIÊN VÀ ĐĂNG KÝ HỌC PHẦN

# 1. Giới thiệu

Hệ thống quản lý sinh viên và đăng ký học phần là ứng dụng được xây dựng bằng ngôn ngữ Java nhằm hỗ trợ quản lý thông tin sinh viên, học phần và quá trình đăng ký môn học. Hệ thống giúp đơn giản hóa công tác quản lý trong các trường học, đồng thời đảm bảo tính chính xác, thuận tiện và hiệu quả trong quá trình đăng ký học phần.

Ứng dụng được phát triển với giao diện đồ họa Java Swing, sử dụng Java Collections để xử lý dữ liệu trong chương trình và kết nối cơ sở dữ liệu MySQL thông qua JDBC để lưu trữ, truy xuất thông tin. Ngoài ra, hệ thống áp dụng xử lý ngoại lệ và xử lý file nhằm nâng cao độ ổn định và hỗ trợ sao lưu, xuất dữ liệu.

# 2. Chức năng chính

# Quản lý sinh viên

- Thêm sinh viên mới
- Cập nhật thông tin sinh viên (ngày tháng năm sinh, mã số sinh viên, địa chỉ, số điện thoại)
- Xoá sinh viên
- Tìm kiếm sinh viên
- Hiển thị danh sách sinh viên
- Kiểm tra tính hợp lệ của dữ liệu trước khi thêm hoặc cập nhật.
- Ngăn chặn việc tạo sinh viên trùng mã số sinh viên

# Quản lý học phần

- Thêm học phần mới
- Cập nhật thông tin học phần, bao gồm mã học phần, tên học phần, số tín chỉ và giảng viên phụ trách
- Xóa học phần khỏi hệ thống khi đáp ứng các điều kiện liên quan
- Tìm kiếm học phần theo mã hoặc tên học phần
- Hiển thị danh sách học phần hiện có
- Kiểm tra tính hợp lệ của thông tin học phần
- Ngăn chặn việc tạo học phần trùng mã

# Quản lý đăng ký học phần

- Cho phép sinh viên đăng ký học phần
- Hiển thị danh sách học phần mà sinh viên đã đăng ký
- Hủy đăng ký học phần
- Tra cứu danh sách sinh viên đăng ký một học phần
- Kiểm tra sinh viên và học phần có tồn tại trước khi đăng ký
- Ngăn chặn sinh viên đăng ký trùng một học phần
-Kiểm tra số lượng học phần hoặc tổng số tín chỉ đăng ký theo giới hạn do hệ thống quy định
- Hiển thị thông tin đăng ký của từng sinh viên

# Thống kê và báo cáo

- Thống kê tổng số sinh viên
- Thống kê tổng số học phần
- Thống kê số lượng sinh viên đăng ký từng học phần
- Thống kê tổng số tín chỉ mỗi sinh viên đã đăng ký
- Tra cứu danh sách sinh viên theo học phần
- Hiển thị kết quả thống kê trên giao diện

#  Quản lý dữ liệu và xử lý file

- Xuất danh sách sinh viên ra file CSV hoặc TXT
- Xuất danh sách học phần ra file CSV hoặc TXT
- Xuất danh sách đăng ký học phần ra file
- Đọc dữ liệu từ file để hỗ trợ nhập dữ liệu ban đầu
- Thông báo khi xuất hoặc đọc file thành công
- Xử lý các trường hợp không tìm thấy file, không có quyền truy cập hoặc dữ liệu trong file không hợp lệ

# Giao diện người dùng

- Xây dựng giao diện bằng Java Swing
- Hiển thị dữ liệu bằng JTable
- Sử dụng JTextField để nhập thông tin
- Sử dụng JButton để thực hiện các thao tác thêm, sửa, xóa, tìm kiếm và đăng ký
- Sử dụng JComboBox để lựa chọn học phần hoặc các giá trị có sẵn
- Sử dụng JOptionPane để hiển thị thông báo, cảnh báo và xác nhận thao tác
- Bố trí giao diện thành các khu vực quản lý sinh viên, quản lý học phần, đăng ký học phần và thống kê

# 3. Công nghệ sử dụng

**- Ngôn ngữ lập trình:** Java

**- Giao diện:** Java Swing

**- Cơ sở dữ liệu:** MySQL

**- Kết nối cơ sở dữ liệu:** JDBC

**- Kiến thức áp dụng:** Lập trình hướng đối tượng (OOP), Java Collections Framework

**- Kỹ thuật bổ sung:** Xử lý file và xử lý ngoại lệ

# 4. Thiết kế cơ sở dữ liệu

# Hệ thống dự kiến gồm ba thực thể chính:

**- Student:** Lưu trữ thông tin sinh viên.

**- Course:** Lưu trữ thông tin học phần.

**- Enrollment:** Lưu trữ thông tin đăng ký và liên kết sinh viên với học phần.
 
Các khóa chính và khóa ngoại giúp duy trì tính toàn vẹn dữ liệu và mối quan hệ giữa các bản ghi.

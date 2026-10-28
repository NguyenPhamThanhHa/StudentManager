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

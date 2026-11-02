
package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.Time;
import java.util.List;

import model.Schedule;
import model.CourseClass;
import Data_Access_Object.ScheduleDAO;
import Data_Access_Object.CourseDAO;

public class ScheduleView extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    private JComboBox<CourseClass> cboClass;
    private JTextField txtDay, txtStart, txtEnd, txtRoom, txtSearch;

    private ScheduleDAO scheduleDAO;
    private CourseDAO courseDAO;

    public ScheduleView() {

        scheduleDAO = new ScheduleDAO();
        courseDAO = new CourseDAO();

        setTitle("Quản lý lịch học");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Tiêu đề
        JLabel title = new JLabel("QUẢN LÝ LỊCH HỌC", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] columns = {
            "Mã lịch", "Mã lớp", "Mã học phần",
            "Tên học phần", "Giảng viên", "Học kỳ",
            "Thứ", "Giờ bắt đầu", "Giờ kết thúc", "Phòng"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Form nhập liệu
        JPanel form = new JPanel(new GridLayout(3, 4, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Thông tin lịch học"));

        cboClass = new JComboBox<>();
        txtDay = new JTextField();
        txtStart = new JTextField();
        txtEnd = new JTextField();
        txtRoom = new JTextField();
        txtSearch = new JTextField();

        form.add(new JLabel("Lớp học phần:"));
        form.add(cboClass);

        form.add(new JLabel("Thứ:"));
        form.add(txtDay);

        form.add(new JLabel("Giờ bắt đầu:"));
        form.add(txtStart);

        form.add(new JLabel("Giờ kết thúc:"));
        form.add(txtEnd);

        form.add(new JLabel("Phòng học:"));
        form.add(txtRoom);

        form.add(new JLabel("Tìm kiếm:"));
        form.add(txtSearch);

        // Các nút
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Cập nhật");
        JButton btnDelete = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm mới");

        JPanel buttons = new JPanel();
        buttons.add(btnAdd);
        buttons.add(btnUpdate);
        buttons.add(btnDelete);
        buttons.add(btnRefresh);

        JPanel bottom = new JPanel(new BorderLayout(5, 5));
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottom.add(form, BorderLayout.CENTER);
        bottom.add(buttons, BorderLayout.SOUTH);

        add(bottom, BorderLayout.SOUTH);

        // Sự kiện
        btnAdd.addActionListener(e -> addSchedule());
        btnUpdate.addActionListener(e -> updateSchedule());
        btnDelete.addActionListener(e -> deleteSchedule());
        btnRefresh.addActionListener(e -> clearForm());

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                searchSchedule();
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedSchedule();
            }
        });

        loadClasses();
        loadSchedules();
    }

    // Tải lớp học phần
    private void loadClasses() {

        try {
            cboClass.removeAllItems();

            List<CourseClass> classes = courseDAO.getAllCourseClasses();

            for (CourseClass c : classes) {
                cboClass.addItem(c);
            }

            cboClass.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(
                        JList<?> list, Object value, int index,
                        boolean isSelected, boolean cellHasFocus) {

                    super.getListCellRendererComponent(
                            list, value, index, isSelected, cellHasFocus);

                    if (value instanceof CourseClass) {
                        CourseClass c = (CourseClass) value;
                        setText("Lớp " + c.getClassId()
                                + " - " + c.getCourseName()
                                + " - " + c.getLecturer());
                    }

                    return this;
                }
            });

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi tải lớp học phần: " + e.getMessage());
        }
    }

    // Tải lịch học
    private void loadSchedules() {

        try {
            tableModel.setRowCount(0);

            List<Schedule> list = scheduleDAO.getAllSchedules();

            for (Schedule s : list) {
                tableModel.addRow(new Object[]{
                    s.getScheduleId(),
                    s.getClassId(),
                    s.getCourseId(),
                    s.getCourseName(),
                    s.getLecturer(),
                    s.getSemester(),
                    s.getDayName(),
                    s.getStartTime(),
                    s.getEndTime(),
                    s.getRoom()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi tải lịch học: " + e.getMessage());
        }
    }

    // Lấy thông tin từ form
    private Schedule getFormData(int id) {

        CourseClass c = (CourseClass) cboClass.getSelectedItem();

        if (c == null) {
            throw new IllegalArgumentException("Vui lòng chọn lớp học phần!");
        }

        String day = txtDay.getText().trim();
        String start = txtStart.getText().trim();
        String end = txtEnd.getText().trim();
        String room = txtRoom.getText().trim();

        if (day.isEmpty() || start.isEmpty()
                || end.isEmpty() || room.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin!");
        }

        Time startTime = Time.valueOf(start);
        Time endTime = Time.valueOf(end);

        if (!startTime.before(endTime)) {
            throw new IllegalArgumentException(
                    "Giờ bắt đầu phải nhỏ hơn giờ kết thúc!");
        }

        return new Schedule(
                id,
                c.getClassId(),
                c.getCourseId(),
                c.getCourseName(),
                day,
                startTime,
                endTime,
                room,
                c.getLecturer(),
                c.getSemester(),
                c.getAcademicYear()
        );
    }

    // Thêm
    private void addSchedule() {

        try {
            Schedule s = getFormData(0);
            scheduleDAO.addSchedule(s);

            JOptionPane.showMessageDialog(this, "Thêm lịch học thành công!");
            clearForm();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Không thể thêm lịch học: " + e.getMessage());
        }
    }

    // Cập nhật
    private void updateSchedule() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn lịch học cần sửa!");
            return;
        }

        try {
            int modelRow = table.convertRowIndexToModel(row);
            int id = Integer.parseInt(
                    tableModel.getValueAt(modelRow, 0).toString());

            Schedule s = getFormData(id);
            scheduleDAO.updateSchedule(s);

            JOptionPane.showMessageDialog(this,
                    "Cập nhật lịch học thành công!");

            clearForm();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Không thể cập nhật: " + e.getMessage());
        }
    }

    // Xóa
    private void deleteSchedule() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn lịch học cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa lịch học?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int modelRow = table.convertRowIndexToModel(row);
            int id = Integer.parseInt(
                    tableModel.getValueAt(modelRow, 0).toString());

            scheduleDAO.deleteSchedule(id);

            JOptionPane.showMessageDialog(this, "Xóa lịch học thành công!");
            clearForm();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Không thể xóa: " + e.getMessage());
        }
    }

    // Hiển thị thông tin khi chọn dòng
    private void showSelectedSchedule() {

        int row = table.getSelectedRow();

        if (row == -1) {
            return;
        }

        int modelRow = table.convertRowIndexToModel(row);

        int classId = Integer.parseInt(
                tableModel.getValueAt(modelRow, 1).toString());

        for (int i = 0; i < cboClass.getItemCount(); i++) {
            CourseClass c = cboClass.getItemAt(i);

            if (c.getClassId() == classId) {
                cboClass.setSelectedIndex(i);
                break;
            }
        }

        txtDay.setText(tableModel.getValueAt(modelRow, 6).toString());
        txtStart.setText(tableModel.getValueAt(modelRow, 7).toString());
        txtEnd.setText(tableModel.getValueAt(modelRow, 8).toString());
        txtRoom.setText(tableModel.getValueAt(modelRow, 9).toString());
    }

    // Tìm kiếm
    private void searchSchedule() {

        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter(
                    "(?i)" + java.util.regex.Pattern.quote(keyword)));
        }
    }

    // Làm mới
    private void clearForm() {

        table.clearSelection();

        txtDay.setText("");
        txtStart.setText("");
        txtEnd.setText("");
        txtRoom.setText("");
        txtSearch.setText("");

        sorter.setRowFilter(null);

        loadClasses();
        loadSchedules();
    }
}

package StudentManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CourseView extends JFrame {
    private JTable courseTable;
    private DefaultTableModel tableModel;
    private JTextField txtCourseID, txtCourseName, txtCredits;
    private JTextArea txtDescription;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;

    public CourseView() {
        setTitle("Quản lý Môn học");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Đóng cửa sổ này không tắt app chính
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ===== TIÊU ĐỀ =====
        JLabel lblTitle = new JLabel("QUẢN LÝ DANH MỤC MÔN HỌC", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // ===== BẢNG HIỂN THỊ DANH SÁCH (BÊN PHẢI) =====
        String[] columnNames = {"Mã môn", "Tên môn học", "Số tín chỉ", "Mô tả"};
        tableModel = new DefaultTableModel(columnNames, 0);
        courseTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(courseTable);
        add(scrollPane, BorderLayout.CENTER);

        // ===== PANEL NHẬP LIỆU VÀ NÚT BẤM (BÊN TRÁI) =====
        JPanel panelLeft = new JPanel();
        panelLeft.setLayout(new BorderLayout());
        panelLeft.setPreferredSize(new Dimension(320, 0));
        panelLeft.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form nhập liệu
        JPanel panelForm = new JPanel(new GridLayout(8, 1, 5, 5));
        
        panelForm.add(new JLabel("Mã môn học:"));
        txtCourseID = new JTextField();
        panelForm.add(txtCourseID);

        panelForm.add(new JLabel("Tên môn học:"));
        txtCourseName = new JTextField();
        panelForm.add(txtCourseName);

        panelForm.add(new JLabel("Số tín chỉ:"));
        txtCredits = new JTextField();
        panelForm.add(txtCredits);

        panelForm.add(new JLabel("Mô tả:"));
        txtDescription = new JTextArea(3, 20);
        JScrollPane descScroll = new JScrollPane(txtDescription);
        panelForm.add(descScroll);

        panelLeft.add(panelForm, BorderLayout.CENTER);

        // Các nút chức năng
        JPanel panelButtons = new JPanel(new GridLayout(2, 2, 8, 8));
        panelButtons.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnAdd = new JButton("Thêm");
        btnUpdate = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnClear = new JButton("Làm mới");

        // Định dạng font chữ cho nút
        Font btnFont = new Font("Arial", Font.BOLD, 12);
        btnAdd.setFont(btnFont);
        btnUpdate.setFont(btnFont);
        btnDelete.setFont(btnFont);
        btnClear.setFont(btnFont);

        panelButtons.add(btnAdd);
        panelButtons.add(btnUpdate);
        panelButtons.add(btnDelete);
        panelButtons.add(btnClear);

        panelLeft.add(panelButtons, BorderLayout.SOUTH);
        add(panelLeft, BorderLayout.WEST);

        // Sự kiện mẫu khi click vào bảng (đưa dữ liệu lên form)
        courseTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = courseTable.getSelectedRow();
                if (row >= 0) {
                    txtCourseID.setText(tableModel.getValueAt(row, 0).toString());
                    txtCourseName.setText(tableModel.getValueAt(row, 1).toString());
                    txtCredits.setText(tableModel.getValueAt(row, 2).toString());
                    txtDescription.setText(tableModel.getValueAt(row, 3) != null ? tableModel.getValueAt(row, 3).toString() : "");
                    txtCourseID.setEditable(false); // Khóa mã môn khi sửa
                }
            }
        });
        
        btnClear.addActionListener(e -> clearForm());
    }

    private void clearForm() {
        txtCourseID.setText("");
        txtCourseName.setText("");
        txtCredits.setText("");
        txtDescription.setText("");
        txtCourseID.setEditable(true);
        courseTable.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CourseView().setVisible(true);
        });
    }
}
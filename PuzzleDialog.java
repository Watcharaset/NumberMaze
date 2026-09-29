import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/** หน้าต่างให้ผู้เล่นเลือกไอเทมจาก Inventory มาเรียงเป็นสมการ */
public class PuzzleDialog extends JDialog {
    private static final Font THAI_FONT = new Font("Tahoma", Font.PLAIN, 16);
    private static final Font BIG_FONT = new Font("Arial", Font.BOLD, 28);

    private final int target;
    private final Inventory inventory;
    private final List<Item> equation = new ArrayList<>();   // ไอเทมที่เลือกมาเรียงแล้ว
    private boolean solved = false;

    private final JLabel equationLabel = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel messageLabel = new JLabel(" ", SwingConstants.CENTER);
    private final JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));

    public PuzzleDialog(Window owner, int target, Inventory inventory) {
        super(owner, "ประตูรหัสผ่าน", ModalityType.APPLICATION_MODAL);
        this.target = target;
        this.inventory = inventory;

        // ส่วนบน: เป้าหมาย
        JLabel targetLabel = new JLabel("จัดเรียงให้ได้ผลลัพธ์ = " + target, SwingConstants.CENTER);
        targetLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        if (Sprites.PUZZLE_DOOR != null) {                    // รูปประตูภารกิจอยู่ด้านบนข้อความ
            Image icon = Sprites.PUZZLE_DOOR.getScaledInstance(80, 90, Image.SCALE_SMOOTH);
            targetLabel.setIcon(new ImageIcon(icon));
            targetLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            targetLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        }

        // ส่วนกลาง: สมการที่กำลังเรียง + ข้อความแจ้งเตือน + ปุ่มไอเทม
        equationLabel.setFont(BIG_FONT);
        messageLabel.setFont(THAI_FONT);
        messageLabel.setForeground(new Color(200, 0, 0));

        JPanel center = new JPanel(new GridLayout(3, 1, 0, 8));
        center.add(equationLabel);
        center.add(messageLabel);
        center.add(itemPanel);

        // ส่วนล่าง: ปุ่มควบคุม
        JButton undoButton = makeButton("ลบตัวสุดท้าย");
        JButton clearButton = makeButton("ล้าง");
        JButton submitButton = makeButton("ตกลง");
        JButton cancelButton = makeButton("ยกเลิก");

        undoButton.addActionListener(e -> {
            if (!equation.isEmpty()) {
                equation.remove(equation.size() - 1);
            }
            refresh();
        });
        clearButton.addActionListener(e -> {
            equation.clear();
            refresh();
        });
        submitButton.addActionListener(e -> submit());
        cancelButton.addActionListener(e -> dispose());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        buttons.add(undoButton);
        buttons.add(clearButton);
        buttons.add(submitButton);
        buttons.add(cancelButton);

        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        root.add(targetLabel, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        root.add(buttons, BorderLayout.SOUTH);
        setContentPane(root);

        refresh();
        setMinimumSize(new Dimension(480, 0));
        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isSolved() {
        return solved;
    }

    /** อัปเดตหน้าจอ: แสดงสมการปัจจุบัน และสร้างปุ่มไอเทมใหม่ */
    private void refresh() {
        // แสดงสมการ
        if (equation.isEmpty()) {
            equationLabel.setText("?");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Item item : equation) {
                sb.append(item.getSymbol()).append(' ');
            }
            equationLabel.setText(sb.toString().trim());
        }

        // สร้างปุ่มจากไอเทมใน Inventory — ไอเทมที่เลือกไปแล้วจะกดซ้ำไม่ได้
        itemPanel.removeAll();
        if (inventory.size() == 0) {
            JLabel empty = new JLabel("กระเป๋าว่างเปล่า — ไปเก็บตัวเลขและเครื่องหมายก่อน");
            empty.setFont(THAI_FONT);
            itemPanel.add(empty);
        }
        for (Item item : inventory.getItems()) {
            JButton b = new JButton(item.getSymbol());
            b.setFont(new Font("Arial", Font.BOLD, 20));
            b.setBackground(item.getColor());
            b.setFocusPainted(false);
            b.setEnabled(!equation.contains(item));
            b.addActionListener(e -> {
                equation.add(item);
                messageLabel.setText(" ");
                refresh();
            });
            itemPanel.add(b);
        }
        itemPanel.revalidate();
        itemPanel.repaint();
    }

    /** ตรวจคำตอบ: ถ้าถูก ลบไอเทมที่ใช้ออกจาก Inventory แล้วปิดหน้าต่าง */
    private void submit() {
        try {
            double result = ExpressionEvaluator.evaluate(equation);
            if (Math.abs(result - target) < 1e-9) {
                for (Item item : equation) {
                    inventory.remove(item);          // ไอเทมที่ใช้แล้วหายไป
                }
                solved = true;
                JOptionPane.showMessageDialog(this, "ถูกต้อง! ประตูเปิดแล้ว", "สำเร็จ",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                messageLabel.setText("ได้ " + ExpressionEvaluator.format(result)
                        + " ยังไม่ตรงกับเป้าหมาย " + target);
            }
        } catch (IllegalArgumentException | ArithmeticException ex) {
            messageLabel.setText(ex.getMessage());   // สมการผิดรูปแบบ หรือ หารด้วยศูนย์
        }
    }

    private JButton makeButton(String text) {
        JButton b = new JButton(text);
        b.setFont(THAI_FONT);
        return b;
    }
}

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import java.awt.Font;
import java.util.Enumeration;

public class Main {
    public static void main(String[] args) {
        useThaiFont();

        JFrame frame = new JFrame("Number Maze");      // สร้างหน้าต่าง
        frame.add(new GamePanel());                    // ใส่พื้นที่วาดเกมลงไป
        frame.pack();                                  // ปรับขนาดหน้าต่างให้พอดีกับ GamePanel
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);             // ให้หน้าต่างอยู่กลางจอ
        frame.setVisible(true);
    }

    /**
     * ฟอนต์เริ่มต้นของ Swing ไม่มีตัวอักษรไทย ทำให้กล่องข้อความ (JOptionPane) แสดงเป็นสี่เหลี่ยม □□□
     * จึงเปลี่ยนฟอนต์เริ่มต้นของทุก component เป็น Tahoma ซึ่งรองรับภาษาไทย
     */
    private static void useThaiFont() {
        FontUIResource thai = new FontUIResource("Tahoma", Font.PLAIN, 14);
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            if (UIManager.get(key) instanceof FontUIResource) {
                UIManager.put(key, thai);
            }
        }
    }
}

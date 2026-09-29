import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * โหลดรูปภาพทั้งหมดของเกมจากโฟลเดอร์ images/ เพียงครั้งเดียวตอนเริ่มโปรแกรม
 * ถ้าหารูปไม่เจอ ค่าจะเป็น null และแต่ละคลาสจะวาดเป็นรูปทรงแทน (เกมยังเล่นได้)
 */
public class Sprites {
    public static final BufferedImage PLAYER      = load("player.png");
    public static final BufferedImage PUZZLE_DOOR = load("puzzle_door.png");
    public static final BufferedImage EXIT_DOOR   = load("exit_door.png");
    public static final BufferedImage GUARD       = load("guard.png");
    public static final BufferedImage WALL        = load("wall.png");
    public static final BufferedImage FLOOR       = load("floor.png");

    private Sprites() { }

    private static BufferedImage load(String name) {
        try {
            URL url = Sprites.class.getResource("/images/" + name);   // กรณีรันจากไฟล์ .jar
            if (url != null) {
                return ImageIO.read(url);
            }
            File file = new File("images", name);                     // กรณีรันจาก VS Code
            if (file.exists()) {
                return ImageIO.read(file);
            }
        } catch (IOException e) {
            System.err.println("โหลดรูป " + name + " ไม่ได้: " + e.getMessage());
        }
        System.err.println("ไม่พบรูป images/" + name + " — จะวาดเป็นรูปทรงแทน");
        return null;
    }

    /**
     * วาดรูปให้พอดีกรอบ w x h โดยไม่บิดสัดส่วน (จัดกึ่งกลางแนวนอน ชิดขอบล่าง)
     */
    public static void drawFit(Graphics g, Image img, int x, int y, int w, int h) {
        int iw = img.getWidth(null), ih = img.getHeight(null);
        double scale = Math.min((double) w / iw, (double) h / ih);
        int dw = (int) (iw * scale), dh = (int) (ih * scale);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, x + (w - dw) / 2, y + (h - dh), dw, dh, null);
    }
}

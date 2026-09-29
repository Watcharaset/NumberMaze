import java.awt.Color;
import java.awt.Graphics;

/** นักสำรวจ — ตัวละครที่ผู้เล่นควบคุม */
public class Player extends GameObject {       // Inheritance: สืบทอดจาก GameObject
    private final Inventory inventory = new Inventory();   // Composition: ผู้เล่น "มี" กระเป๋า

    public Player(int x, int y) {
        super(x, y);                            // เรียก Constructor ของคลาสแม่
    }

    public void moveTo(int newX, int newY) {
        x = newX;
        y = newY;
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void draw(Graphics g, int tile) {
        if (Sprites.PLAYER != null) {
            // ตัวละครสูงกว่าช่อง จึงวาดสูง 1.3 เท่า โดยเท้าอยู่ขอบล่างของช่อง
            int h = (int) (tile * 1.3);
            Sprites.drawFit(g, Sprites.PLAYER, x * tile, (y + 1) * tile - h, tile, h);
        } else {
            g.setColor(Color.BLUE);             // ไม่มีรูป → วาดวงกลมสีน้ำเงินแทน
            g.fillOval(x * tile + 6, y * tile + 6, tile - 12, tile - 12);
        }
    }
}

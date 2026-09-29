import java.awt.Graphics;

/**
 * คลาสแม่ของทุกสิ่งในเกม (ผู้เล่น, ไอเทม, ประตู ...)
 * เป็น abstract เพราะ "วัตถุในเกม" เฉยๆ ไม่มีหน้าตา ต้องให้คลาสลูกกำหนดเองว่าจะวาดอย่างไร
 */
public abstract class GameObject {
    protected int x, y;   // ตำแหน่งบนตาราง (x = คอลัมน์, y = แถว) — protected ให้คลาสลูกใช้ได้

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    /** คลาสลูกทุกตัวต้อง implement เมธอดนี้ (Abstract method) */
    public abstract void draw(Graphics g, int tileSize);
}

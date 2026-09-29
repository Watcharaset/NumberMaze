import java.awt.*;

/**
 * ไอเทมที่เก็บได้ในเขาวงกต (ตัวเลข หรือ เครื่องหมาย)
 * วิธีวาดเหมือนกันทุกไอเทม แต่ "สัญลักษณ์" และ "สี" ให้คลาสลูกเป็นคนกำหนด
 */
public abstract class Item extends GameObject {

    public Item(int x, int y) {
        super(x, y);
    }

    /** ข้อความที่แสดงบนไอเทม เช่น "3" หรือ "+" */
    public abstract String getSymbol();

    /** สีพื้นหลังของไอเทม */
    public abstract Color getColor();

    /** วาดไอเทมบนแผนที่ตามตำแหน่งในตาราง */
    @Override
    public void draw(Graphics g, int tile) {
        drawAt(g, x * tile, y * tile, tile);
    }

    /** วาดไอเทมที่ตำแหน่งพิกเซลใดก็ได้ (ใช้ทั้งบนแผนที่และในแถบ Inventory) */
    public void drawAt(Graphics g, int px, int py, int size) {
        int pad = size / 8;
        g.setColor(getColor());                               // Polymorphism: แต่ละคลาสลูกให้สีต่างกัน
        g.fillRoundRect(px + pad, py + pad, size - pad * 2, size - pad * 2, 10, 10);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, size / 2));
        FontMetrics fm = g.getFontMetrics();
        String s = getSymbol();                               // Polymorphism: แต่ละคลาสลูกให้สัญลักษณ์ต่างกัน
        int textX = px + (size - fm.stringWidth(s)) / 2;      // จัดให้อยู่กึ่งกลางช่อง
        int textY = py + (size + fm.getAscent() - fm.getDescent()) / 2;
        g.drawString(s, textX, textY);
    }
}

import javax.swing.SwingUtilities;
import java.awt.*;

/** ประตูภารกิจ — ต้องจัดเรียงสมการให้ได้ผลลัพธ์เท่ากับ target จึงจะเปิด */
public class PuzzleDoor extends Door {
    private final int target;

    public PuzzleDoor(int x, int y, int target) {
        super(x, y);
        this.target = target;
    }

    public int getTarget() {
        return target;
    }

    /** เปิดหน้าต่างแก้สมการ ถ้าแก้ถูกประตูจะปลดล็อก */
    @Override
    public void interact(Player player, GamePanel game) {
        Window owner = SwingUtilities.getWindowAncestor(game);
        PuzzleDialog dialog = new PuzzleDialog(owner, target, player.getInventory());
        dialog.setVisible(true);                // หน้าต่างแบบ modal: โค้ดจะหยุดรอจนกว่าจะปิดหน้าต่าง
        if (dialog.isSolved()) {
            unlock();
        }
    }

    @Override
    public void draw(Graphics g, int tile) {
        int px = x * tile, py = y * tile;
        if (Sprites.PUZZLE_DOOR != null) {
            drawWithSprite((Graphics2D) g, px, py, tile);
            return;
        }
        if (isLocked()) {
            // ประตูปิด: สีส้ม + แสดงเป้าหมาย
            g.setColor(Color.ORANGE);
            g.fillRect(px, py, tile, tile);
            g.setColor(new Color(120, 60, 0));
            g.drawRect(px + 2, py + 2, tile - 5, tile - 5);

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            String s = "=" + target;
            FontMetrics fm = g.getFontMetrics();
            g.drawString(s, px + (tile - fm.stringWidth(s)) / 2, py + (tile + fm.getAscent()) / 2 - 2);
        } else {
            // ประตูเปิด: เหลือแค่กรอบประตู เดินผ่านได้
            g.setColor(new Color(160, 100, 40));
            g.fillRect(px, py, 6, tile);
            g.fillRect(px + tile - 6, py, 6, tile);
        }
    }

    private void drawWithSprite(Graphics2D g, int px, int py, int tile) {
        if (!isLocked()) {
            // ประตูเปิดแล้ว: วาดจางๆ ให้รู้ว่าเคยมีประตูตรงนี้
            Composite old = g.getComposite();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            Sprites.drawFit(g, Sprites.PUZZLE_DOOR, px, py, tile, tile);
            g.setComposite(old);
            return;
        }
        Sprites.drawFit(g, Sprites.PUZZLE_DOOR, px, py, tile, tile);

        // ป้ายเป้าหมายสีดำด้านล่างประตู เช่น "=16"
        String s = "=" + target;
        g.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g.getFontMetrics();
        int w = fm.stringWidth(s) + 8, h = 15;
        int lx = px + (tile - w) / 2, ly = py + tile - h;
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRoundRect(lx, ly, w, h, 8, 8);
        g.setColor(new Color(255, 215, 0));
        g.drawString(s, lx + 4, ly + h - 3);
    }
}

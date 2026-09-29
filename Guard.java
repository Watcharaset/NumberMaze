import java.awt.*;

/** ผู้พิทักษ์ — เดินลาดตระเวนซ้าย-ขวา ชนกำแพงหรือประตูแล้วเดินกลับ ห้ามเดินชน! */
public class Guard extends Obstacle {
    private static final int MOVE_EVERY = 2;    // ขยับทุก 2 tick (0.4 วินาที)

    private int direction = 1;                  // 1 = ไปทางขวา, -1 = ไปทางซ้าย
    private int tick = 0;

    public Guard(int x, int y) {
        super(x, y);
    }

    @Override
    public void update(Level level) {
        tick++;
        if (tick % MOVE_EVERY != 0) {
            return;
        }
        if (level.isBlockedForGuard(x + direction, y)) {
            direction = -direction;             // ข้างหน้าตัน → หันกลับ
        }
        if (!level.isBlockedForGuard(x + direction, y)) {
            x += direction;
        }
    }

    @Override
    public boolean isDangerous() {
        return true;                            // ผู้พิทักษ์อันตรายตลอดเวลา
    }

    @Override
    public String getHitMessage() {
        return "ผู้พิทักษ์จับได้! กลับไปจุดเริ่มต้น";
    }

    @Override
    public void draw(Graphics g, int tile) {
        int px = x * tile, py = y * tile;
        if (Sprites.GUARD != null) {
            Sprites.drawFit(g, Sprites.GUARD, px + 2, py + 2, tile - 4, tile - 4);
            return;
        }
        g.setColor(new Color(200, 30, 30));
        g.fillRoundRect(px + 5, py + 5, tile - 10, tile - 10, 12, 12);

        // ดวงตามองไปทางที่กำลังเดิน
        g.setColor(Color.WHITE);
        int eyeY = py + tile / 3;
        g.fillOval(px + tile / 3 - 4 + direction * 3, eyeY, 8, 8);
        g.fillOval(px + 2 * tile / 3 - 4 + direction * 3, eyeY, 8, 8);
    }
}

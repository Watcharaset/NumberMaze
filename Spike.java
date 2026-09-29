import java.awt.*;

/** กับดักหนาม — โผล่ขึ้น-หดลงเป็นจังหวะ ต้องรอจังหวะที่หนามหดแล้วค่อยเดินผ่าน */
public class Spike extends Obstacle {
    private static final int UP_TICKS = 5;      // หนามโผล่ 5 tick (1 วินาที)
    private static final int CYCLE_TICKS = 11;  // ครบรอบ 11 tick (หดลง 1.2 วินาที)

    private int tick = 0;

    public Spike(int x, int y) {
        super(x, y);
    }

    @Override
    public void update(Level level) {
        tick = (tick + 1) % CYCLE_TICKS;
    }

    @Override
    public boolean isDangerous() {
        return tick < UP_TICKS;
    }

    @Override
    public String getHitMessage() {
        return "โดนหนาม! กลับไปจุดเริ่มต้น";
    }

    @Override
    public void draw(Graphics g, int tile) {
        int px = x * tile, py = y * tile;
        g.setColor(new Color(150, 130, 120));                // พื้นกับดัก
        g.fillRect(px + 2, py + 2, tile - 4, tile - 4);

        if (isDangerous()) {
            // วาดหนามสามเหลี่ยม 3 อัน
            g.setColor(new Color(220, 40, 40));
            int w = tile / 3;
            for (int i = 0; i < 3; i++) {
                int left = px + i * w;
                g.fillPolygon(new int[]{left + 2, left + w / 2, left + w - 2},
                              new int[]{py + tile - 6, py + 8, py + tile - 6}, 3);
            }
        } else {
            // หนามหด เหลือแค่รู
            g.setColor(new Color(90, 80, 70));
            for (int i = 0; i < 3; i++) {
                g.fillOval(px + 6 + i * (tile / 3), py + tile / 2 - 3, 6, 6);
            }
        }
    }
}

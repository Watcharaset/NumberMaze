import java.awt.*;

/** ประตูชัย — ไม่ต้องไขรหัส เดินชนแล้วผ่านด่านทันที */
public class ExitDoor extends Door {

    public ExitDoor(int x, int y) {
        super(x, y);
    }

    @Override
    public void interact(Player player, GamePanel game) {
        game.completeLevel();
    }

    @Override
    public void draw(Graphics g, int tile) {
        int px = x * tile, py = y * tile;
        if (Sprites.EXIT_DOOR != null) {
            Sprites.drawFit(g, Sprites.EXIT_DOOR, px, py, tile, tile);
            return;
        }
        g.setColor(new Color(40, 180, 70));
        g.fillRect(px, py, tile, tile);
        g.setColor(new Color(255, 215, 0));                  // ขอบสีทอง
        g.drawRect(px + 2, py + 2, tile - 5, tile - 5);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g.getFontMetrics();
        g.drawString("EXIT", px + (tile - fm.stringWidth("EXIT")) / 2, py + (tile + fm.getAscent()) / 2 - 2);
    }
}

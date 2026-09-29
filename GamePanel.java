import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {           // Inheritance: สืบทอดจาก JPanel
    private static final int TILE = 44;           // ขนาดช่องละ 44 พิกเซล
    private static final int HUD_HEIGHT = 112;    // ความสูงแถบข้อมูลด้านล่าง
    private static final Font THAI_FONT = new Font("Tahoma", Font.BOLD, 15);

    private int levelIndex;
    private Level level;                          // Composition: GamePanel "มี" Level
    private Player player;                        // Composition: GamePanel "มี" Player
    private String message = "";                  // ข้อความแจ้งเตือนใน HUD
    private boolean gameWon = false;
    private final Timer timer;                    // นาฬิกาของเกม ใช้ขยับอุปสรรค

    public GamePanel() {
        loadLevel(0);
        setFocusable(true);                      // ต้องเปิด ไม่งั้น JPanel จะรับปุ่มกดไม่ได้

        // ฟังการกดปุ่มคีย์บอร์ด
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameWon) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        gameWon = false;
                        loadLevel(0);             // เล่นใหม่ตั้งแต่ด่านแรก
                    }
                    return;
                }
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W, KeyEvent.VK_UP    -> tryMove(0, -1);   // ขึ้น
                    case KeyEvent.VK_S, KeyEvent.VK_DOWN  -> tryMove(0, 1);    // ลง
                    case KeyEvent.VK_A, KeyEvent.VK_LEFT  -> tryMove(-1, 0);   // ซ้าย
                    case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> tryMove(1, 0);    // ขวา
                    case KeyEvent.VK_R -> {                                    // เริ่มด่านใหม่
                        loadLevel(levelIndex);
                        message = "เริ่มด่านใหม่แล้ว";
                    }
                }
            }
        });

        // ทุก 0.2 วินาที ให้อุปสรรคขยับ แล้วเช็คว่าผู้เล่นโดนหรือไม่
        timer = new Timer(200, e -> tick());
        timer.start();
    }

    /** โหลดด่านใหม่: สร้างแผนที่และผู้เล่นใหม่ (กระเป๋าว่าง) */
    private void loadLevel(int index) {
        levelIndex = index;
        level = Levels.create(index);
        player = new Player(level.getStartX(), level.getStartY());
        message = "";

        // แต่ละด่านขนาดไม่เท่ากัน → ปรับขนาดหน้าต่างให้พอดีกับแผนที่
        setPreferredSize(new Dimension(level.getWidth() * TILE, level.getHeight() * TILE + HUD_HEIGHT));
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {                     // ตอนเริ่มเกมครั้งแรกยังไม่มีหน้าต่าง Main จะ pack() ให้เอง
            window.pack();
            window.setLocationRelativeTo(null);
        }
        repaint();
    }

    private void tick() {
        if (gameWon) {
            return;
        }
        for (Obstacle obstacle : level.getObstacles()) {
            obstacle.update(level);               // Polymorphism: หนามกับผู้พิทักษ์ขยับไม่เหมือนกัน
        }
        checkObstacles();
        repaint();
    }

    /** ถ้าผู้เล่นยืนอยู่ช่องเดียวกับอุปสรรคที่กำลังอันตราย → ส่งกลับจุดเริ่มต้น (ไอเทมยังอยู่ในกระเป๋า) */
    private void checkObstacles() {
        for (Obstacle obstacle : level.getObstacles()) {
            if (obstacle.isDangerous() && obstacle.getX() == player.getX() && obstacle.getY() == player.getY()) {
                message = obstacle.getHitMessage();
                player.moveTo(level.getStartX(), level.getStartY());
                return;
            }
        }
    }

    /** พยายามเดินไป dx, dy ช่อง ถ้าเป็นกำแพงจะเดินไม่ได้ ถ้าเป็นประตูที่ล็อกอยู่จะเรียก interact() */
    private void tryMove(int dx, int dy) {
        int newX = player.getX() + dx;
        int newY = player.getY() + dy;

        if (level.isWall(newX, newY)) {
            return;                               // ชนกำแพง → ไม่ขยับ
        }

        Door door = level.findDoorAt(newX, newY);
        if (door != null && door.isLocked()) {
            timer.stop();                         // หยุดเวลาระหว่างแก้สมการ ผู้พิทักษ์จะได้ไม่เดินมาชน
            door.interact(player, this);          // Polymorphism: PuzzleDoor เปิดหน้าต่างสมการ, ExitDoor จบด่าน
            timer.start();
            repaint();
            requestFocusInWindow();               // เอาโฟกัสคีย์บอร์ดกลับมาที่เกมหลังปิดหน้าต่าง
            return;
        }

        player.moveTo(newX, newY);

        Item item = level.takeItemAt(newX, newY);
        if (item != null) {
            player.getInventory().add(item);
            message = "เก็บ " + item.getSymbol() + " แล้ว";
        }
        checkObstacles();
        repaint();
    }

    /** ExitDoor เรียกเมธอดนี้เมื่อผู้เล่นเข้าประตูชัย */
    public void completeLevel() {
        if (levelIndex + 1 < Levels.count()) {
            JOptionPane.showMessageDialog(this, "ผ่าน" + level.getName() + " แล้ว!\nไปด่านต่อไปกันเลย",
                    "ผ่านด่าน", JOptionPane.INFORMATION_MESSAGE);
            loadLevel(levelIndex + 1);
        } else {
            gameWon = true;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {   // Polymorphism: override เมธอดของ JPanel
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 1) พื้นและกำแพง
        for (int row = 0; row < level.getHeight(); row++) {
            for (int col = 0; col < level.getWidth(); col++) {
                boolean wall = level.isWall(col, row);
                Image tileImage = wall ? Sprites.WALL : Sprites.FLOOR;
                if (tileImage != null) {
                    g.drawImage(tileImage, col * TILE, row * TILE, TILE, TILE, null);
                } else {
                    g.setColor(wall ? new Color(60, 60, 70) : new Color(205, 205, 200));
                    g.fillRect(col * TILE, row * TILE, TILE, TILE);
                }
            }
        }

        // 2) วาดทุกอย่างในด่าน — แต่ละ Object รู้วิธีวาดตัวเอง (Polymorphism)
        for (Item item : level.getItems())             item.draw(g, TILE);
        for (Door door : level.getDoors())             door.draw(g, TILE);
        for (Obstacle obstacle : level.getObstacles()) obstacle.draw(g, TILE);
        player.draw(g, TILE);

        // 3) แถบข้อมูลด้านล่าง
        drawHud(g);

        // 4) หน้าจอชนะ
        if (gameWon) {
            drawWinScreen(g);
        }
    }

    private void drawHud(Graphics g) {
        int top = level.getHeight() * TILE;
        g.setColor(new Color(30, 30, 40));
        g.fillRect(0, top, getWidth(), HUD_HEIGHT);

        // บรรทัดที่ 1: ชื่อด่าน (ซ้าย) + วิธีเริ่มใหม่ (ขวา)
        g.setFont(THAI_FONT);
        FontMetrics fm = g.getFontMetrics();
        g.setColor(Color.WHITE);
        g.drawString(level.getName() + "  (" + (levelIndex + 1) + "/" + Levels.count() + ")", 10, top + 22);

        g.setColor(new Color(160, 160, 170));
        String hint = "R = เริ่มด่านใหม่";
        g.drawString(hint, getWidth() - fm.stringWidth(hint) - 10, top + 22);

        // บรรทัดที่ 2: ข้อความแจ้งเตือน (กึ่งกลาง)
        g.setColor(new Color(255, 210, 90));
        g.drawString(message, (getWidth() - fm.stringWidth(message)) / 2, top + 46);

        // บรรทัดที่ 3: Inventory
        g.setColor(Color.WHITE);
        g.drawString("กระเป๋า:", 10, top + 88);
        int x = 80;
        for (Item item : player.getInventory().getItems()) {
            item.drawAt(g, x, top + 58, 46);
            x += 46;
        }
    }

    private void drawWinScreen(Graphics g) {
        g.setColor(new Color(0, 0, 0, 190));                  // พื้นดำโปร่งแสง
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setFont(new Font("Arial", Font.BOLD, 56));
        FontMetrics fm = g.getFontMetrics();
        String title = "YOU SURVIVED!";
        int y = getHeight() / 2 - 10;
        g.setColor(new Color(255, 215, 0));
        g.drawString(title, (getWidth() - fm.stringWidth(title)) / 2, y);

        g.setFont(new Font("Tahoma", Font.PLAIN, 20));
        fm = g.getFontMetrics();
        String sub = "คุณหนีออกจากเขาวงกตได้สำเร็จ — กด Enter เพื่อเล่นใหม่";
        g.setColor(Color.WHITE);
        g.drawString(sub, (getWidth() - fm.stringWidth(sub)) / 2, y + 50);
    }
}

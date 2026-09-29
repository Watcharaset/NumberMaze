import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * ด่านหนึ่งด่าน — อ่านแผนที่จากข้อความแล้วสร้าง Object ต่างๆ ในด่าน
 *
 * สัญลักษณ์ในแผนที่:
 *   #  กำแพง            .  ทางเดิน          P  จุดเริ่มต้นผู้เล่น
 *   0-9  ตัวเลข          + - * /  เครื่องหมาย
 *   a-z  ประตูภารกิจ (เป้าหมายกำหนดใน doorTargets)
 *   E  ประตูชัย          ^  กับดักหนาม       G  ผู้พิทักษ์
 */
public class Level {
    private final String name;
    private final char[][] grid;
    private final List<Item> items = new ArrayList<>();
    private final List<Door> doors = new ArrayList<>();
    private final List<Obstacle> obstacles = new ArrayList<>();
    private int startX, startY;

    public Level(String name, String[] map, Map<Character, Integer> doorTargets) {
        this.name = name;
        grid = new char[map.length][];
        for (int row = 0; row < map.length; row++) {
            grid[row] = map[row].toCharArray();
            for (int col = 0; col < grid[row].length; col++) {
                char c = grid[row][col];
                if (c == '#' || c == '.') {
                    continue;                                   // กำแพง/ทางเดิน เก็บไว้ใน grid ตามเดิม
                }

                if (c == 'P') {
                    startX = col;
                    startY = row;
                } else if (Character.isDigit(c)) {
                    items.add(new NumberItem(col, row, c - '0'));
                } else if ("+-*/".indexOf(c) >= 0) {
                    items.add(new OperatorItem(col, row, c));
                } else if (c >= 'a' && c <= 'z') {
                    Integer target = doorTargets.get(c);
                    if (target == null) {
                        throw new IllegalStateException("ประตู '" + c + "' ใน " + name + " ยังไม่ได้กำหนดเป้าหมาย");
                    }
                    doors.add(new PuzzleDoor(col, row, target));
                } else if (c == 'E') {
                    doors.add(new ExitDoor(col, row));
                } else if (c == '^') {
                    obstacles.add(new Spike(col, row));
                } else if (c == 'G') {
                    obstacles.add(new Guard(col, row));
                } else {
                    throw new IllegalStateException("ไม่รู้จักสัญลักษณ์ '" + c + "' ใน " + name);
                }
                grid[row][col] = '.';                           // ช่องที่มี Object อยู่ ถือเป็นทางเดิน
            }
        }
    }

    public String getName()   { return name; }
    public int getWidth()     { return grid[0].length; }
    public int getHeight()    { return grid.length; }
    public int getStartX()    { return startX; }
    public int getStartY()    { return startY; }

    public boolean isWall(int x, int y) {
        return grid[y][x] == '#';
    }

    /** ผู้พิทักษ์เดินผ่านกำแพงและประตูไม่ได้ (จะได้อยู่แต่ในห้องของตัวเอง) */
    public boolean isBlockedForGuard(int x, int y) {
        return isWall(x, y) || findDoorAt(x, y) != null;
    }

    /** หยิบไอเทมที่ (x, y) ออกจากแผนที่ ถ้าไม่มีคืนค่า null */
    public Item takeItemAt(int x, int y) {
        for (Item item : items) {
            if (item.getX() == x && item.getY() == y) {
                items.remove(item);
                return item;
            }
        }
        return null;
    }

    public Door findDoorAt(int x, int y) {
        for (Door door : doors) {
            if (door.getX() == x && door.getY() == y) {
                return door;
            }
        }
        return null;
    }

    public List<Item> getItems()         { return Collections.unmodifiableList(items); }
    public List<Door> getDoors()         { return Collections.unmodifiableList(doors); }
    public List<Obstacle> getObstacles() { return Collections.unmodifiableList(obstacles); }
}

/**
 * คลาสแม่ของอุปสรรค ถ้าผู้เล่นโดนตอนที่อุปสรรค "อันตราย" จะถูกส่งกลับจุดเริ่มต้นของด่าน
 * อุปสรรคแต่ละแบบเปลี่ยนสถานะตามเวลาต่างกัน จึงให้คลาสลูก implement update() เอง
 */
public abstract class Obstacle extends GameObject {

    public Obstacle(int x, int y) {
        super(x, y);
    }

    /** ถูกเรียกทุก tick (ทุก 0.2 วินาที) */
    public abstract void update(Level level);

    /** ตอนนี้ถ้าผู้เล่นอยู่ช่องเดียวกันจะโดนหรือไม่ */
    public abstract boolean isDangerous();

    /** ข้อความที่แสดงเมื่อผู้เล่นโดน */
    public abstract String getHitMessage();
}

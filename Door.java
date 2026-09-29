/**
 * คลาสแม่ของประตูทุกชนิด
 * ประตูทุกบานมีสถานะ ล็อก/ไม่ล็อก แต่ "ชนแล้วเกิดอะไรขึ้น" ให้คลาสลูกกำหนดเอง
 */
public abstract class Door extends GameObject implements Interactable {
    private boolean locked = true;              // ประตูเริ่มต้นล็อกเสมอ

    public Door(int x, int y) {
        super(x, y);
    }

    public boolean isLocked() {
        return locked;
    }

    /** protected: ให้เฉพาะคลาสลูก (เช่น PuzzleDoor) เป็นคนปลดล็อกได้ คลาสอื่นปลดล็อกเองไม่ได้ */
    protected void unlock() {
        locked = false;
    }
}

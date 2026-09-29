import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** กระเป๋าเก็บไอเทมของผู้เล่น */
public class Inventory {
    private final List<Item> items = new ArrayList<>();   // Encapsulation: ข้างนอกแก้ list ตรงๆ ไม่ได้

    public void add(Item item) {
        items.add(item);
    }

    public void remove(Item item) {
        items.remove(item);
    }

    /** คืน list แบบอ่านอย่างเดียว เพื่อไม่ให้คลาสอื่นแอบเพิ่ม/ลบไอเทมเอง */
    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public int size() {
        return items.size();
    }
}

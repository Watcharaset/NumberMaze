/** สิ่งที่ผู้เล่นเดินชนแล้ว "มีปฏิกิริยา" เช่น ประตูรหัสผ่าน, ประตูชัย */
public interface Interactable {
    void interact(Player player, GamePanel game);
}

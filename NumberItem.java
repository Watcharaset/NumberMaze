import java.awt.Color;

/** ไอเทมตัวเลข 0-9 */
public class NumberItem extends Item {
    private final int value;

    public NumberItem(int x, int y, int value) {
        super(x, y);
        this.value = value;
    }

    public int getValue() { return value; }

    @Override
    public String getSymbol() { return String.valueOf(value); }

    @Override
    public Color getColor() { return new Color(255, 230, 120); }   // สีเหลือง
}

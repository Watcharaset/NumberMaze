import java.awt.Color;

/** ไอเทมเครื่องหมาย + - * / */
public class OperatorItem extends Item {
    private final char operator;

    public OperatorItem(int x, int y, char operator) {
        super(x, y);
        this.operator = operator;
    }

    public char getOperator() { return operator; }

    @Override
    public String getSymbol() { return String.valueOf(operator); }

    @Override
    public Color getColor() { return new Color(140, 200, 255); }   // สีฟ้า
}

import java.util.ArrayList;
import java.util.List;

/**
 * คำนวณสมการจากไอเทม ตามลำดับความสำคัญทางคณิตศาสตร์จริง (คูณ/หาร ก่อน บวก/ลบ)
 * เช่น 3 + 2 * 4 = 11 (ไม่ใช่ 20)
 */
public class ExpressionEvaluator {

    /** ตรวจรูปแบบสมการ: ต้องเป็น ตัวเลข เครื่องหมาย ตัวเลข เครื่องหมาย ... ตัวเลข */
    public static void validate(List<Item> tokens) {
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("ยังไม่ได้เลือกไอเทมเลย");
        }
        for (int i = 0; i < tokens.size(); i++) {
            boolean shouldBeNumber = (i % 2 == 0);          // ตำแหน่งคู่ = ตัวเลข, ตำแหน่งคี่ = เครื่องหมาย
            Item token = tokens.get(i);
            if (shouldBeNumber && !(token instanceof NumberItem)) {
                throw new IllegalArgumentException("ต้องสลับ ตัวเลข กับ เครื่องหมาย และขึ้นต้นด้วยตัวเลข");
            }
            if (!shouldBeNumber && !(token instanceof OperatorItem)) {
                throw new IllegalArgumentException("ตัวเลขสองตัวติดกันไม่ได้ ต้องมีเครื่องหมายคั่น");
            }
        }
        if (tokens.size() % 2 == 0) {
            throw new IllegalArgumentException("สมการต้องจบด้วยตัวเลข");
        }
        if (tokens.size() < 3) {
            throw new IllegalArgumentException("ต้องใช้เครื่องหมายอย่างน้อย 1 ตัว");
        }
    }

    /**
     * คำนวณ 2 รอบ
     * รอบที่ 1: ไล่จากซ้ายไปขวา ทำ * และ / ทันที ส่วน + และ - เก็บไว้ก่อน
     * รอบที่ 2: นำตัวเลขที่เหลือมา + และ - จากซ้ายไปขวา
     */
    public static double evaluate(List<Item> tokens) {
        validate(tokens);

        List<Double> numbers = new ArrayList<>();
        List<Character> operators = new ArrayList<>();
        numbers.add(valueOf(tokens.get(0)));

        // รอบที่ 1: * และ /
        for (int i = 1; i < tokens.size(); i += 2) {
            char op = ((OperatorItem) tokens.get(i)).getOperator();
            double next = valueOf(tokens.get(i + 1));
            int last = numbers.size() - 1;

            if (op == '*') {
                numbers.set(last, numbers.get(last) * next);
            } else if (op == '/') {
                if (next == 0) {
                    throw new ArithmeticException("หารด้วยศูนย์ไม่ได้");
                }
                numbers.set(last, numbers.get(last) / next);
            } else {
                operators.add(op);          // + หรือ - เก็บไว้ทำรอบที่ 2
                numbers.add(next);
            }
        }

        // รอบที่ 2: + และ -
        double result = numbers.get(0);
        for (int i = 0; i < operators.size(); i++) {
            if (operators.get(i) == '+') {
                result += numbers.get(i + 1);
            } else {
                result -= numbers.get(i + 1);
            }
        }
        return result;
    }

    /** แสดงผลลัพธ์: ถ้าเป็นจำนวนเต็มไม่ต้องมีทศนิยม เช่น 11 แทน 11.0 */
    public static String format(double value) {
        if (value == Math.rint(value)) {
            return String.valueOf((long) value);
        }
        return String.format("%.2f", value);
    }

    private static double valueOf(Item item) {
        return ((NumberItem) item).getValue();
    }
}

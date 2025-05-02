package bg.sofia.uni.fmi.myfitnesspal.client.items;

import bg.sofia.uni.fmi.myfitnesspal.client.items.tracker.ConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.client.items.tracker.WaterConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.client.serializer.visitor.ItemVisitor;

import java.time.LocalDate;

public class Water extends Consumable {

    public void drink(LocalDate date, int quantity) {
        if (date == null) {
            throw new IllegalArgumentException("date is null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        consumpt(date, quantity);
    }

    @Override
    protected ConsumptionEntry createConsumptionEntry(Object[] args) {
        if (args.length < 1 || !(args[0] instanceof Integer)) {
            throw new IllegalArgumentException(
                    "Water consumption requires quantity");
        }
        int quantity = (Integer) args[0];
        return new WaterConsumptionEntry(quantity);
    }

    @Override
    public String toString() {
        return "Water";
    }

    @Override
    public void accept(ItemVisitor visitor) {
        visitor.visitWater(this);
    }
}

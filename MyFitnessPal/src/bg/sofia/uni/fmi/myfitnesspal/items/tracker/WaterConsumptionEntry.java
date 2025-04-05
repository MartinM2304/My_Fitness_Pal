package bg.sofia.uni.fmi.myfitnesspal.items.tracker;

public class WaterConsumptionEntry extends ConsumptionEntry {
    private final int quantity;

    public WaterConsumptionEntry(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return String.format("%d ml", quantity);
    }
}
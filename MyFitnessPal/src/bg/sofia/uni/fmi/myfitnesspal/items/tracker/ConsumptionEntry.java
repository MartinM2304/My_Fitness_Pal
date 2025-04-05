package bg.sofia.uni.fmi.myfitnesspal.items.tracker;

import java.io.Serializable;

public abstract class ConsumptionEntry implements Serializable {
    public abstract int getQuantity();

    @Override
    public abstract String toString();
}
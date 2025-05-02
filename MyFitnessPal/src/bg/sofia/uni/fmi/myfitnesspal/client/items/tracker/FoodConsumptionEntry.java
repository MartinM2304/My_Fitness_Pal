package bg.sofia.uni.fmi.myfitnesspal.client.items.tracker;

import bg.sofia.uni.fmi.myfitnesspal.client.items.MealTime;

public class FoodConsumptionEntry extends ConsumptionEntry {
    private final int servings;
    private final MealTime mealTime;

    public FoodConsumptionEntry(int servings, MealTime mealTime) {
        this.servings = servings;
        this.mealTime = mealTime;
    }

    @Override
    public int getQuantity() {
        return servings;
    }

    public int getServings() {
        return servings;
    }

    public MealTime getMealTime() {
        return mealTime;
    }

    @Override
    public String toString() {
        return String.format("%d servings (%d g) at %s", servings,
                getQuantity(), mealTime.toString());
    }
}

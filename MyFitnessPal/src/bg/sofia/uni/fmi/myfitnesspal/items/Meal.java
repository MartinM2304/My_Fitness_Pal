package bg.sofia.uni.fmi.myfitnesspal.items;

import bg.sofia.uni.fmi.myfitnesspal.items.tracker.ConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.serializer.visitor.ItemVisitor;

import java.util.ArrayList;
import java.util.List;

public class Meal extends Consumable {
    private String name;
    private String description;
    private int servings;
    private List<Food> foods = new ArrayList<>();

    public Meal(String name) {
        this.name = name;
    }

    public Meal addFood(Food food) {
        foods.add(food);
        return this;
    }

    public String getName() {
        return name;
    }

    public List<Food> getFoods() {
        return foods;
    }

    @Override
    protected ConsumptionEntry createConsumptionEntry(Object[] args) {

        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name + ":\n");
        for (Food food : foods) {
            sb.append(food).append("\n");
        }
        return sb.toString();
    }


    @Override
    public void accept(ItemVisitor visitor) {
        visitor.visitMeal(this);
    }
}

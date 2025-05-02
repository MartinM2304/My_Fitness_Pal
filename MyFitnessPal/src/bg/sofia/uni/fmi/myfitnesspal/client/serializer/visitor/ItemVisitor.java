package bg.sofia.uni.fmi.myfitnesspal.client.serializer.visitor;

import bg.sofia.uni.fmi.myfitnesspal.client.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Meal;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Water;

public interface ItemVisitor {
    void visitFood(Food food);
    void visitWater(Water water);
    void visitMeal(Meal meal);
}

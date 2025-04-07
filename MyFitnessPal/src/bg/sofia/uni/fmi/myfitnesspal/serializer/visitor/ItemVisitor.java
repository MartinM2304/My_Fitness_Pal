package bg.sofia.uni.fmi.myfitnesspal.serializer.visitor;

import bg.sofia.uni.fmi.myfitnesspal.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.items.Meal;
import bg.sofia.uni.fmi.myfitnesspal.items.Water;

public interface ItemVisitor {
    void visitFood(Food food);
    void visitWater(Water water);
    void visitMeal(Meal meal);
}

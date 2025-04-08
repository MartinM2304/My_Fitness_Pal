package bg.sofia.uni.fmi.myfitnesspal.serializer.visitor;

import bg.sofia.uni.fmi.myfitnesspal.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.items.Meal;
import bg.sofia.uni.fmi.myfitnesspal.items.Water;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Map;

public class JsonItemVisitor implements ItemVisitor {
    private JsonObject result;
    private final Map<Integer, String> foodIds;

    public JsonItemVisitor(Map<Integer, String> foodIds) {
        this.foodIds = foodIds;
        this.result = new JsonObject();
    }

    @Override
    public void visitFood(Food food) {
        result = new JsonObject();
        int foodId = foodIds.entrySet().stream()
                .filter(entry -> entry.getValue().equals(food.toString()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(-1);
        result.addProperty("type", "Food");
        result.addProperty("foodId", foodId);
        result.addProperty("name", food.getName());
        result.addProperty("description", food.getDescription());
        result.addProperty("servingSize", food.getServingSize());
        result.addProperty("servingsPerContainer", food.getServingsPerContainer());
        result.addProperty("calories", food.getCalories());
        result.addProperty("carbs", food.getCarbs());
        result.addProperty("fat", food.getFat());
        result.addProperty("protein", food.getProtein());
    }

    @Override
    public void visitWater(Water water) {
        result = new JsonObject();
        result.addProperty("type", "Water");
    }

    @Override
    public void visitMeal(Meal meal) {
        result = new JsonObject();
        result.addProperty("type", "Meal");
        result.addProperty("name", meal.getName());
        JsonArray foodIdsArray = new JsonArray();
        meal.getFoods().forEach(food -> {
            int foodId = foodIds.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(food.toString()))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(-1);
            foodIdsArray.add(foodId);
        });
        result.add("foodIds", foodIdsArray);
    }

    public JsonObject getResult() {
        return result;
    }
}
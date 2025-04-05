package bg.sofia.uni.fmi.myfitnesspal.items;

import bg.sofia.uni.fmi.myfitnesspal.items.tracker.ConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.FoodConsumptionEntry;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Food extends Consumable {
    private final String name;
    private final String description;
    private final int servingSize;
    private final int servingsPerContainer;
    private final int calories;
    private final double carbs;
    private final double fat;
    private final double protein;

    private Food(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.servingSize = builder.servingSize;
        this.servingsPerContainer = builder.servingsPerContainer;
        this.calories = builder.calories;
        this.carbs = builder.carbs;
        this.fat = builder.fat;
        this.protein = builder.protein;
    }

    @Override
    protected ConsumptionEntry createConsumptionEntry(Object[] args) {
        if (args.length < 2 || !(args[0] instanceof Integer) || !(args[1] instanceof MealTime)) {
            throw new IllegalArgumentException("Food consumption requires servings and mealTime");
        }
        int servings = (Integer) args[0];
        MealTime mealTime = (MealTime) args[1];
        if (servings <= 0) {
            throw new IllegalArgumentException("Servings must be positive");
        }
        return new FoodConsumptionEntry(servings, mealTime, servingSize);
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getServingSize() {
        return servingSize;
    }

    public int getCalories() {
        return calories;
    }

    @Override
    public String toString() {
        return String.format("%s (%dg; %d kcal; %.1fg, %.1fg, %.2fg)",
                name, servingSize, calories, carbs, fat, protein);
    }

    public double getProtein() {
        return protein;
    }

    public double getFat() {
        return fat;
    }

    public double getCarbs() {
        return carbs;
    }

    public int getServingsPerContainer() {
        return servingsPerContainer;
    }

    @Override
    public JsonElement getJsonElement(com.google.gson.JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "Food");
        json.addProperty("name", name);
        json.addProperty("description", description);
        json.addProperty("servingSize", servingSize);
        json.addProperty("servingsPerContainer", servingsPerContainer);
        json.addProperty("calories", calories);
        json.addProperty("carbs", carbs);
        json.addProperty("fat", fat);
        json.addProperty("protein", protein);
        json.add("consumptionLog", context.serialize(consumptionLog));
        return json;
    }

    @Override
    public void deserialize(JsonObject json, com.google.gson.JsonDeserializationContext context) {
        if (!json.has("consumptionLog")) return;
        Type logType = new com.google.gson.reflect.TypeToken<Map<LocalDate, List<ConsumptionEntry>>>() {}.getType();
        Map<LocalDate, List<ConsumptionEntry>> log = context.deserialize(json.get("consumptionLog"), logType);
        for (Map.Entry<LocalDate, List<ConsumptionEntry>> entry : log.entrySet()) {
            for (ConsumptionEntry ce : entry.getValue()) {
                if (ce instanceof FoodConsumptionEntry fce) {
                    consumpt(entry.getKey(), fce.getServings(), fce.getMealTime());
                }
            }
        }
    }

    public static class Builder {
        private final String name;
        private int servingSize;
        private int servingsPerContainer;
        private int calories;
        private double carbs;
        private double fat;
        private double protein;
        private String description = "";

        public Builder(String name) {
            this.name = name;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder servingSize(int servingSize) {
            this.servingSize = servingSize;
            return this;
        }

        public Builder servingsPerContainer(int servingsPerContainer) {
            this.servingsPerContainer = servingsPerContainer;
            return this;
        }

        public Builder calories(int calories) {
            this.calories = calories;
            return this;
        }

        public Builder carbs(double carbs) {
            this.carbs = carbs;
            return this;
        }

        public Builder fat(double fat) {
            this.fat = fat;
            return this;
        }

        public Builder protein(double protein) {
            this.protein = protein;
            return this;
        }

        public Food build() {
            return new Food(this);
        }
    }
}
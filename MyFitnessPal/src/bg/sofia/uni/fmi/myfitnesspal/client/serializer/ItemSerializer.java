package bg.sofia.uni.fmi.myfitnesspal.client.serializer;

import bg.sofia.uni.fmi.myfitnesspal.client.Controller;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Meal;
import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategy;
import bg.sofia.uni.fmi.myfitnesspal.client.serializer.strategy.SerializationStrategy;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class ItemSerializer {
    private final String fileName;
    private final Map<String, Consumable> items;
    private final Map<Integer, String> foodIds;
    private final SerializationStrategy strategy;
    private final Controller controller;

    public ItemSerializer(String fileName, Map<String, Consumable> items,
                          Map<Integer, String> foodIds, Controller controller,
                          IOStrategy ioStrategy) {
        this.fileName = fileName;
        this.items = items;
        this.foodIds = foodIds;
        this.strategy = SerializationFactory.createStrategy("json", foodIds,
                ioStrategy);
        this.controller = controller;
    }

    public boolean saveData() {
        try {
            strategy.serialize(items, fileName);
            System.out.println("Data saved to " + fileName);
            return true;
        } catch (RuntimeException e) {
            System.err.println("Error saving data: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean readData() {
        try {
            File f = new File(fileName);
            if (!f.exists() && !f.isDirectory()) {
                return false;
            }
            Map<String, Consumable> loadedItems =
                    strategy.deserialize(fileName);
            if (loadedItems.isEmpty()) {
                System.out.println("No existing data loaded.");
                return false;
            }
            items.clear();
            items.putAll(loadedItems);
            populateFoodIdsAndMealIds();
            populateMealFoods();
            System.out.println("Loaded items: " + items);
            return true;
        } catch (RuntimeException e) {
            System.err.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void populateFoodIdsAndMealIds() {
        try (FileReader reader = new FileReader(fileName)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            foodIds.clear();
            int maxFoodId = 0;
            int maxMealId = 0;

            for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                JsonObject itemJson = entry.getValue().getAsJsonObject();
                String type = itemJson.has("type")
                        ? itemJson.get("type").getAsString() : null;

                if ("Food".equals(type)) {
                    int foodId = itemJson.get("foodId").getAsInt();
                    //String foodName = itemJson.get("name").getAsString();
                    foodIds.put(foodId, entry.getKey());
                    maxFoodId = Math.max(maxFoodId, foodId);
                } else if ("Meal".equals(type)) {
                    String mealName = itemJson.get("name").getAsString();
                    maxMealId++;
                    items.values().stream()
                            .filter(Meal.class::isInstance)
                            .map(Meal.class::cast)
                            .filter(m -> m.getName().equals(mealName))
                            .findFirst()
                            .ifPresent(meal -> {
                            });
                }
            }
            controller.setCurrentFoodId(maxFoodId);
            controller.setCurrentMealId(maxMealId);
        } catch (IOException e) {
            System.err.println(
                    "Error populating foodIds and mealIds: " + e.getMessage());
        }
    }

    private void populateMealFoods() {
        try (FileReader reader = new FileReader(fileName)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            items.values().stream()
                    .filter(Meal.class::isInstance)
                    .map(Meal.class::cast)
                    .forEach(meal -> {
                        meal.getFoods().clear();
                        JsonObject mealJson =
                                json.get(meal.getName()).getAsJsonObject();
                        if (mealJson != null && mealJson.has("foodIds")) {
                            JsonArray foodIdsArray =
                                    mealJson.get("foodIds").getAsJsonArray();
                            foodIdsArray.forEach(foodId -> {
                                int id = foodId.getAsInt();
                                String foodKey = foodIds.get(id);
                                Consumable foodItem = items.get(foodKey);
                                if (foodItem instanceof Food f) {
                                    meal.addFood(f);
                                }
                            });
                        }
                    });
        } catch (IOException e) {
            System.err.println(
                    "Error populating meal foods: " + e.getMessage());
        }
    }

    public Map<String, Consumable> getItems() {
        return items;
    }
}

package bg.sofia.uni.fmi.myfitnesspal;

import bg.sofia.uni.fmi.myfitnesspal.commands.Command;
import bg.sofia.uni.fmi.myfitnesspal.commands.CommandFactory;
import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.items.Meal;
import bg.sofia.uni.fmi.myfitnesspal.serializer.ItemSerializer;
import bg.sofia.uni.fmi.myfitnesspal.items.Water;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Controller {
    public static final String FILE_NAME = "storage.json";
    private Map<String, Consumable> items = new HashMap<>();
    private Map<Integer, String> foodIds = new HashMap<>();
    private Map<Integer, String> mealIds = new HashMap<>();
    private int currentFoodId = 0;
    private int currentMealId = 0;
    private CommandFactory commandFactory;
    private final Scanner sc = new Scanner(System.in);
    private ItemSerializer serializer;

    public Controller() {
        initController();
    }

    public Controller(ItemSerializer serializer,
                      CommandFactory commandFactory) {
        this.serializer = serializer;
        this.commandFactory = commandFactory;
        this.items.putIfAbsent("water", new Water());
    }

    public Command executeCommand(String command) {
        Command executedCommand = commandFactory.getCommand(command);
        if (executedCommand.isExitCommand()) {
            serializer.saveData();
        }
        executedCommand.execute();
        return executedCommand;
    }

    private void initController() {
        serializer = new ItemSerializer(FILE_NAME, items, foodIds, this);
        if (!serializer.readData()) {
            System.out.println(
                    "No existing data loaded. Initializing new storage.");
        }

        items.putIfAbsent("water", new Water());
        if (items.isEmpty() ||
                (items.size() == 1 && items.containsKey("water"))) {
            fillFoodIDs();
            fillMealIDs();
        }
        commandFactory = new CommandFactory(items, sc, this);
    }

    public void addFood(Food food) {
        items.put(food.toString(), food);
        int newFoodId = updateCurrentFoodId();
        foodIds.put(newFoodId, food.getName());
        System.out.println(
                "Added food with ID: " + newFoodId + " -> " + food.getName());
    }

    public void addMeal(Meal menu) {
        currentMealId++;
        mealIds.put(currentMealId, menu.getName());
        items.put(menu.getName(), menu);
    }

    public Map<String, Consumable> getItems() {
        return items;
    }

    public Map<Integer, String> getFoodIds() {
        return foodIds;
    }

    public Map<Integer, String> getMealIds() {
        return mealIds;
    }

    public int getCurrentFoodId() {
        return currentFoodId;
    }

    public int getCurrentMealId() {
        return currentMealId;
    }

    public int updateCurrentFoodId() {
        currentFoodId++;
        return currentFoodId;
    }

    public int setCurrentFoodId(int currentFoodId) {
        this.currentFoodId = currentFoodId;
        return this.currentFoodId;
    }

    public int setCurrentMealId(int currentMealId) {
        this.currentMealId = currentMealId;
        return this.currentMealId;
    }

    public int updateCurrentFoodId(int currentFoodId) {
        this.currentFoodId = currentFoodId;
        return this.currentFoodId;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine();
            try {
                executeCommand(command);
            } catch (IllegalArgumentException e) {
                System.out.println("wrong command try another");
            }
        }

    }

    private Map<Integer, String> fillFoodIDs() {
        currentFoodId = 0;
        for (Map.Entry<String, Consumable> entry : items.entrySet()) {
            if (entry.getValue() instanceof Food) {
                currentFoodId++;
                foodIds.put(currentFoodId, ((Food) entry.getValue()).getName());
            }
        }
        return foodIds;
    }

    private Map<Integer, String> fillMealIDs() {
        currentMealId = 0;
        mealIds.clear();
        for (Map.Entry<String, Consumable> entry : items.entrySet()) {
            if (entry.getValue() instanceof Meal) {
                currentMealId++;
                mealIds.put(currentMealId, entry.getKey());
            }
        }
        return mealIds;
    }
}

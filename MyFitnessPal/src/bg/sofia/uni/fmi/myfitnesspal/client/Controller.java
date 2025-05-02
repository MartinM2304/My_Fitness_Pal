package bg.sofia.uni.fmi.myfitnesspal.client;

import bg.sofia.uni.fmi.myfitnesspal.client.commands.Command;
import bg.sofia.uni.fmi.myfitnesspal.client.commands.CommandFactory;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Meal;
import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategiesFactory;
import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategy;
import bg.sofia.uni.fmi.myfitnesspal.client.serializer.ItemSerializer;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Water;

import javax.imageio.stream.IIOByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class Controller {
    private final String nickname;
    private final String storagePath;
    private Map<String, Consumable> items = new HashMap<>();
    private Map<Integer, String> foodIds = new HashMap<>();
    private Map<Integer, String> mealIds = new HashMap<>();
    private int currentFoodId = 0;
    private int currentMealId = 0;
    private CommandFactory commandFactory;
    private IOStrategy ioStrategy;
    private ItemSerializer serializer;

    public Controller(String nickname, IOStrategy ioStrategy) {
        this.nickname = nickname;
        this.storagePath = "data/" + nickname + "/storage.json";
        initController(ioStrategy);
    }

    public Controller(String nickname, ItemSerializer serializer,
                      CommandFactory commandFactory,IOStrategy ioStrategy) {
        this.nickname = nickname;
        this.storagePath = "data/" + nickname + "/storage.json";
        this.serializer = serializer;
        this.commandFactory = commandFactory;
        this.ioStrategy = ioStrategy;
        this.items.putIfAbsent("water", new Water());
    }

    public Command executeCommand(String command) {
        try {
            Command executedCommand = commandFactory.getCommand(command);
            if (executedCommand.isExitCommand()) {
                serializer.saveData();
            }
            executedCommand.execute();
            return executedCommand;
        } catch (IllegalArgumentException e) {
            ioStrategy.display("wrong command try another");
            return null;
        }
    }

    private void initController(IOStrategy ioStrategy) {
        serializer = new ItemSerializer(storagePath, items, foodIds, this,
                ioStrategy);
        this.ioStrategy = ioStrategy;
        if (!serializer.readData()) {
            ioStrategy.display(
                    "No existing data loaded. Initializing new storage.");
        }

        items.putIfAbsent("water", new Water());
        if (items.isEmpty() ||
                (items.size() == 1 && items.containsKey("water"))) {
            fillFoodIDs();
            fillMealIDs();
        }
        commandFactory = new CommandFactory(items, ioStrategy, this);
    }

    public void addFood(Food food) {
        items.put(food.toString(), food);
        int newFoodId = updateCurrentFoodId();
        foodIds.put(newFoodId, food.toString());
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

    public void displayFoods() {
        for (Map.Entry<Integer, String> entry : foodIds.entrySet()) {
            Consumable food = items.get(entry.getValue());
            if (food != null) {
                ioStrategy.display(entry.getKey() + ". " + food);
            }
        }
    }

    public void start() {
        while (true) {
            ioStrategy.display("> ");

            String command=ioStrategy.getUserInput();

            try {
                executeCommand(command);
            } catch (IllegalArgumentException e) {
                ioStrategy.display("wrong command try another");
            }
        }
    }


        public String getNickname() {
            return nickname;
        }

        public Map<Integer, String> fillFoodIDs () {
            currentFoodId = 0;
            for (Map.Entry<String, Consumable> entry : items.entrySet()) {
                if (entry.getValue() instanceof Food) {
                    currentFoodId++;
                    foodIds.put(currentFoodId,
                            ((Food) entry.getValue()).getName());
                }
            }
            return foodIds;
        }

        public Map<Integer, String> fillMealIDs () {
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
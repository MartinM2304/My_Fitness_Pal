package bg.sofia.uni.fmi.myfitnesspal.serializer;

import bg.sofia.uni.fmi.myfitnesspal.Controller;
import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.items.Meal;
import bg.sofia.uni.fmi.myfitnesspal.items.Water;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemSerializerTest {

    private ItemSerializer serializer;
    private Map<String, Consumable> items;
    private Map<Integer, String> foodIds;
    private Controller controllerMock;
    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        items = new HashMap<>();
        foodIds = new HashMap<>();
        controllerMock = mock(Controller.class);
        tempFile = Files.createTempFile("test", ".json");
        serializer = new ItemSerializer(tempFile.toString(), items, foodIds, controllerMock);
    }

    @Test
    void testSaveData_Success() throws IOException {
        Food food = new Food.Builder("Apple").servingSize(100).calories(52).build();
        items.put(food.toString(), food);
        foodIds.put(1, food.toString());

        boolean result = serializer.saveData();

        assertTrue(result);
        assertTrue(Files.exists(tempFile));
        String content = Files.readString(tempFile);
        assertTrue(content.contains("\"Apple (100g; 52 kcal; 0.0g, 0.0g, 0.00g)\""));
        assertTrue(content.contains("\"foodId\": 1"));
    }

    @Test
    void testSaveData_IOException() throws IOException {
        Path dir = Files.createTempDirectory("testDir");
        serializer = new ItemSerializer(dir.toString(), items, foodIds, controllerMock);

        boolean result = serializer.saveData();

        assertFalse(result);
    }

    @Test
    void testReadData_SuccessWithFoodAndMeal() throws IOException {
        Food food = new Food.Builder("Apple").servingSize(100).calories(52).build();
        Meal meal = new Meal("Breakfast");
        meal.addFood(food);
        items.put(food.toString(), food);
        items.put("Breakfast", meal);
        foodIds.put(1, food.toString());
        serializer.saveData();

        items.clear();
        foodIds.clear();
        boolean result = serializer.readData();

        assertTrue(result);
        assertEquals(2, items.size());
        assertTrue(items.containsKey("Apple (100g; 52 kcal; 0.0g, 0.0g, 0.00g)"));
        assertTrue(items.containsKey("Breakfast"));
        assertEquals(1, foodIds.size());
        assertEquals("Apple (100g; 52 kcal; 0.0g, 0.0g, 0.00g)", foodIds.get(1));
        assertEquals(1, ((Meal) items.get("Breakfast")).getFoods().size());
        verify(controllerMock).setCurrentFoodId(1);
        verify(controllerMock).setCurrentMealId(1);
    }

    @Test
    void testReadData_FileNotExists() throws IOException {
        Files.deleteIfExists(tempFile);

        boolean result = serializer.readData();

        assertFalse(result);
        assertTrue(items.isEmpty());
        assertTrue(foodIds.isEmpty());
    }

    @Test
    void testReadData_EmptyJson() throws IOException {
        Files.writeString(tempFile, "{}");

        boolean result = serializer.readData();

        assertTrue(result);
        assertTrue(items.isEmpty());
        assertTrue(foodIds.isEmpty());
    }

    @Test
    void testReadData_InvalidJson() throws IOException {
        Files.writeString(tempFile, "not a json");

        boolean result = serializer.readData();

        assertFalse(result);
        assertTrue(items.isEmpty());
        assertTrue(foodIds.isEmpty());
    }

    @Test
    void testPopulateFoodIdsAndMealIdsIOException() throws IOException {
        Files.writeString(tempFile, "{\"invalid\": \"data\"}");
        when(controllerMock.getItems()).thenReturn(items); // Mock to avoid NPE

        serializer.readData(); // Trigger populate methods

        assertFalse(items.isEmpty()); // Partial load might occur
        verify(controllerMock, never()).setCurrentFoodId(anyInt());
    }

    @Test
    void testGetItems() {
        assertEquals(items, serializer.getItems());
    }
}
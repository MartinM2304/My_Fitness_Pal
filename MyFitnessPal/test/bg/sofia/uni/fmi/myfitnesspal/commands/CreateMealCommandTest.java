package bg.sofia.uni.fmi.myfitnesspal.commands;

import bg.sofia.uni.fmi.myfitnesspal.Controller;
import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.items.Meal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateMealCommandTest {

    private CreateMealCommand command;
    private Controller controllerMock;
    private Scanner scannerMock;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        controllerMock = mock(Controller.class);
        scannerMock = mock(Scanner.class);
        command = new CreateMealCommand(controllerMock, scannerMock);
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void testExecute_EmptyMealName() {
        when(scannerMock.nextLine()).thenReturn("");

        Command result = command.execute();

        assertNull(result);
        assertTrue(outContent.toString().contains("Meal name cannot be empty."));
    }

    @Test
    void testExecute_NoFoodsAvailable() {
        when(scannerMock.nextLine()).thenReturn("Breakfast");
        when(controllerMock.getFoodIds()).thenReturn(new HashMap<>());

        Command result = command.execute();

        assertNull(result);
        assertTrue(outContent.toString().contains("No foods available to add to Meal."));
    }

    @Test
    void testExecute_InvalidFoodIdFormat() {
        Map<Integer, String> foodIds = new HashMap<>();
        foodIds.put(1, "Apple (100g; 52 kcal; 0.0g, 0.0g, 0.00g)");
        when(scannerMock.nextLine()).thenReturn("Breakfast", "invalid", "done");
        when(controllerMock.getFoodIds()).thenReturn(foodIds);
        when(controllerMock.getItems()).thenReturn(new HashMap<>());

        Command result = command.execute();

        assertEquals(command, result);
        assertTrue(outContent.toString().contains("Invalid food ID. Use a number or 'done'."));
    }

    @Test
    void testExecute_FoodIdNotFound() {
        Map<Integer, String> foodIds = new HashMap<>();
        foodIds.put(1, "Apple (100g; 52 kcal; 0.0g, 0.0g, 0.00g)");
        when(scannerMock.nextLine()).thenReturn("Breakfast", "2", "done");
        when(controllerMock.getFoodIds()).thenReturn(foodIds);
        when(controllerMock.getItems()).thenReturn(new HashMap<>());

        Command result = command.execute();

        assertEquals(command, result);
        assertTrue(outContent.toString().contains("Food ID not found."));
    }

    @Test
    void testExecute_SuccessWithFood() {
        Map<Integer, String> foodIds = new HashMap<>();
        foodIds.put(1, "Apple (100g; 52 kcal; 0.0g, 0.0g, 0.00g)");
        Food food = new Food.Builder("Apple").servingSize(100).calories(52).build();
        Map<String, Consumable> items = new HashMap<>();
        items.put(food.toString(), food);
        when(scannerMock.nextLine()).thenReturn("Breakfast", "1", "done");
        when(controllerMock.getFoodIds()).thenReturn(foodIds);
        when(controllerMock.getItems()).thenReturn(items);
        when(controllerMock.getCurrentMealId()).thenReturn(1);

        Command result = command.execute();

        assertEquals(command, result);
        verify(controllerMock).addMeal(any(Meal.class));
        assertTrue(outContent.toString().contains("Apple (100g; 52 kcal; 0.0g, 0.0g, 0.00g) added to Breakfast"));
        assertTrue(outContent.toString().contains("Meal 'Breakfast' (ID: 1) created successfully!"));
    }

    @Test
    void testIsExitCommand() {
        assertFalse(command.isExitCommand());
    }

    @Test
    void testToString() {
        assertEquals("create Meal", command.toString());
    }
}
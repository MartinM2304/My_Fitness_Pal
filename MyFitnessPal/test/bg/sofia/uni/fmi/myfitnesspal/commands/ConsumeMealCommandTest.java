package bg.sofia.uni.fmi.myfitnesspal.commands;

import bg.sofia.uni.fmi.myfitnesspal.Controller;
import bg.sofia.uni.fmi.myfitnesspal.date.DateParser;
import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.items.Meal;
import bg.sofia.uni.fmi.myfitnesspal.items.MealTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsumeMealCommandTest {

    private ConsumeMealCommand command;
    private Controller controllerMock;
    private Scanner scannerMock;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        controllerMock = mock(Controller.class);
        scannerMock = mock(Scanner.class);
        command = new ConsumeMealCommand(controllerMock, scannerMock);
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void testExecute_NoMenusAvailable() {
        when(controllerMock.getMealIds()).thenReturn(new HashMap<>());

        Command result = command.execute();

        assertNull(result);
        assertTrue(outContent.toString().contains("No menus available to consume."));
    }

    @Test
    void testExecute_InvalidMenuIdFormat() {
        Map<Integer, String> menuIds = new HashMap<>();
        menuIds.put(1, "Breakfast");
        when(controllerMock.getMealIds()).thenReturn(menuIds);
        when(scannerMock.nextLine()).thenReturn("invalid");

        Command result = command.execute();

        assertNull(result);
        assertTrue(outContent.toString().contains("Invalid menu ID. Use a number."));
    }

    @Test
    void testExecute_MenuIdNotFound() {
        Map<Integer, String> menuIds = new HashMap<>();
        menuIds.put(1, "Breakfast");
        when(controllerMock.getMealIds()).thenReturn(menuIds);
        when(scannerMock.nextLine()).thenReturn("2");

        Command result = command.execute();

        assertNull(result);
        assertTrue(outContent.toString().contains("Menu ID not found."));
    }

    @Test
    void testExecute_MenuNotInItems() {
        Map<Integer, String> menuIds = new HashMap<>();
        menuIds.put(1, "Breakfast");
        when(controllerMock.getMealIds()).thenReturn(menuIds);
        when(controllerMock.getItems()).thenReturn(new HashMap<>());
        when(scannerMock.nextLine()).thenReturn("1");

        Command result = command.execute();

        assertNull(result);
        assertTrue(outContent.toString().contains("Menu not found."));
    }

    @Test
    void testExecute_InvalidDateFormat() {
        Map<Integer, String> menuIds = new HashMap<>();
        menuIds.put(1, "Breakfast");
        Meal meal = new Meal("Breakfast");
        Map<String, Consumable> items = new HashMap<>();
        items.put("Breakfast", meal);
        when(controllerMock.getMealIds()).thenReturn(menuIds);
        when(controllerMock.getItems()).thenReturn(items);
        when(scannerMock.nextLine()).thenReturn("1", "invalid-date");

        try (var mockStatic = Mockito.mockStatic(DateParser.class)) {
            mockStatic.when(() -> DateParser.parse("invalid-date"))
                    .thenThrow(new IllegalArgumentException("Invalid date"));
            Command result = command.execute();

            assertNull(result);
            assertTrue(outContent.toString().contains("Invalid date format. Use: YYYY-MM-DD"));
        }
    }

    @Test
    void testExecute_InvalidMealTime() {
        Map<Integer, String> menuIds = new HashMap<>();
        menuIds.put(1, "Breakfast");
        Meal meal = new Meal("Breakfast");
        Map<String, Consumable> items = new HashMap<>();
        items.put("Breakfast", meal);
        when(controllerMock.getMealIds()).thenReturn(menuIds);
        when(controllerMock.getItems()).thenReturn(items);
        when(scannerMock.nextLine()).thenReturn("1", "2023-11-11", "InvalidMeal");

        try (var mockStatic = Mockito.mockStatic(DateParser.class)) {
            mockStatic.when(() -> DateParser.parse("2023-11-11")).thenReturn(LocalDate.of(2023, 11, 11));
            Command result = command.execute();

            assertNull(result);
            assertTrue(outContent.toString().contains("Invalid meal time. Use: Breakfast, Lunch, Snacks, Dinner"),
                    "Expected invalid meal time message in output: " + outContent.toString());
        }
    }

    @Test
    void testExecute_SuccessWithFood() {
        Map<Integer, String> menuIds = new HashMap<>();
        menuIds.put(1, "Breakfast");
        Meal meal = new Meal("Breakfast");
        Food food = new Food.Builder("Apple").servingSize(100).calories(52).build();
        meal.addFood(food);
        Map<String, Consumable> items = new HashMap<>();
        items.put("Breakfast", meal);
        when(controllerMock.getMealIds()).thenReturn(menuIds);
        when(controllerMock.getItems()).thenReturn(items);
        when(scannerMock.nextLine()).thenReturn("1", "2023-11-11", "Breakfast", "2");

        try (var mockStatic = Mockito.mockStatic(DateParser.class)) {
            mockStatic.when(() -> DateParser.parse("2023-11-11")).thenReturn(LocalDate.of(2023, 11, 11));
            Command result = command.execute();

            assertEquals(command, result);
            String expectedOutput = "200g X Apple (100g; 52 kcal; 0.0g, 0.0g, 0.00g) consumed for BREAKFAST on 2023-11-11";
            assertTrue(outContent.toString().contains("Menu 'Breakfast' consumed successfully!"));
        }
    }

    @Test
    void testExecute_InvalidServingsSkipped() {
        Map<Integer, String> menuIds = new HashMap<>();
        menuIds.put(1, "Breakfast");
        Meal meal = new Meal("Breakfast");
        Food food = new Food.Builder("Apple").servingSize(100).calories(52).build();
        meal.addFood(food);
        Map<String,     Consumable> items = new HashMap<>();
        items.put("Breakfast", meal);
        when(controllerMock.getMealIds()).thenReturn(menuIds);
        when(controllerMock.getItems()).thenReturn(items);
        when(scannerMock.nextLine()).thenReturn("1", "2023-11-11", "Breakfast", "invalid");

        try (var mockStatic = Mockito.mockStatic(DateParser.class)) {
            mockStatic.when(() -> DateParser.parse("2023-11-11")).thenReturn(LocalDate.of(2023, 11, 11));
            Command result = command.execute();

            assertEquals(command, result);
            assertTrue(outContent.toString().contains("Invalid number of servings. Use a number."));
            assertTrue(outContent.toString().contains("Menu 'Breakfast' consumed successfully!"));
        }
    }

    @Test
    void testIsExitCommand() {
        assertFalse(command.isExitCommand());
    }

    @Test
    void testToString() {
        assertEquals("consume menu", command.toString());
    }
}
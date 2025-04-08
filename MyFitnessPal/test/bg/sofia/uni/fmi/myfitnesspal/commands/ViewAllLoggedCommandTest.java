package bg.sofia.uni.fmi.myfitnesspal.commands;

import bg.sofia.uni.fmi.myfitnesspal.Controller;
import bg.sofia.uni.fmi.myfitnesspal.date.DateParser;
import bg.sofia.uni.fmi.myfitnesspal.items.*;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.FoodConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.WaterConsumptionEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ViewAllLoggedCommandTest {

    private ViewAllLoggedCommand command;
    private Controller controllerMock;
    private Scanner scannerMock;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        controllerMock = mock(Controller.class);
        scannerMock = mock(Scanner.class);
        command = new ViewAllLoggedCommand(controllerMock, scannerMock);
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void testExecute_InvalidDateFormat() {
        when(scannerMock.nextLine()).thenReturn("invalid-date");

        try (var mockStatic = Mockito.mockStatic(DateParser.class)) {
            mockStatic.when(() -> DateParser.parse("invalid-date"))
                    .thenThrow(new IllegalArgumentException("wrong date format"));
            assertThrows(IllegalArgumentException.class, () -> command.execute());
        }
    }

    @Test
    void testExecute_NoConsumption() {
        when(scannerMock.nextLine()).thenReturn("2023-11-11");
        when(controllerMock.getItems()).thenReturn(new HashMap<>());

        try (var mockStatic = Mockito.mockStatic(DateParser.class)) {
            mockStatic.when(() -> DateParser.parse("2023-11-11")).thenReturn(LocalDate.of(2023, 11, 11));
            Command result = command.execute();

            assertEquals(command, result);
            for (MealTime mealTime : MealTime.values()) {
                assertTrue(outContent.toString().contains(mealTime.name() + ":\n—\n"));
            }
            assertTrue(outContent.toString().contains("Water: 0.0L"));
        }
    }

    @Test
    void testExecute_WithFoodAndWater() {
        when(scannerMock.nextLine()).thenReturn("2023-11-11");
        LocalDate date = LocalDate.of(2023, 11, 11);
        Food food = new Food.Builder("Apple").servingSize(100).calories(52).carbs(14.0).fat(0.2).protein(0.3).build();
        food.consumpt(date, 2, MealTime.BREAKFAST);
        Water water = new Water();
        water.drink(date, 500);
        Map<String, Consumable> items = new HashMap<>();
        items.put("Apple", food);
        items.put("water", water);
        when(controllerMock.getItems()).thenReturn(items);

        try (var mockStatic = Mockito.mockStatic(DateParser.class)) {
            mockStatic.when(() -> DateParser.parse("2023-11-11")).thenReturn(date);
            Command result = command.execute();

            assertEquals(command, result);
            assertTrue(outContent.toString().contains("BREAKFAST:\n>Apple (200g; 104 kcal; 28.00g, 0.40g, 0.60g)"));
            assertTrue(outContent.toString().contains("Water: 0.5L"));
        }
    }

    @Test
    void testExecute_WithMeal() {
        when(scannerMock.nextLine()).thenReturn("2023-11-11");
        LocalDate date = LocalDate.of(2023, 11, 11);
        Meal meal = new Meal("Breakfast");
        Food food = new Food.Builder("Apple").servingSize(100).calories(52).build();
        meal.addFood(food);
        meal.consumpt(date, 2, MealTime.BREAKFAST);
        Map<String, Consumable> items = new HashMap<>();
        items.put("Breakfast", meal);
        when(controllerMock.getItems()).thenReturn(items);

        try (var mockStatic = Mockito.mockStatic(DateParser.class)) {
            mockStatic.when(() -> DateParser.parse("2023-11-11")).thenReturn(date);
            Command result = command.execute();

            assertEquals(command, result);
            assertTrue(outContent.toString().contains("BREAKFAST:\n>Apple (200g; 104 kcal; 0.00g, 0.00g, 0.00g)"));
        }
    }

    @Test
    void testIsExitCommand() {
        assertFalse(command.isExitCommand());
    }

    @Test
    void testToString() {
        assertEquals("view all logged", command.toString());
    }
}
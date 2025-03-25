package bg.sofia.uni.fmi.myfitnesspal.commands;

import bg.sofia.uni.fmi.myfitnesspal.Controller;
import bg.sofia.uni.fmi.myfitnesspal.date.DateParser;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LogFoodCommandTest {
    private Controller controllerMock;
    private Scanner scannerMock;
    private LogFoodCommand logFoodCommand;
    private Map<Integer, String> foodIds;
    private Map<String, Food> items;

    @BeforeEach
    void setUp() {
        controllerMock = mock(Controller.class);
        scannerMock = mock(Scanner.class);
        foodIds = new HashMap<>();
        items = new HashMap<>();
        logFoodCommand = new LogFoodCommand(controllerMock, scannerMock);

        Food food = new Food.Builder("Apple").servingSize(100).calories(52).carbs(14.0).fat(0.2).protein(0.3).build();
        foodIds.put(1, food.toString());
        items.put(food.toString(), food);
        when(controllerMock.getFoodIds()).thenReturn(foodIds);
        when(controllerMock.getItems()).thenReturn(new HashMap<>(items));
    }

    @Test
    void testExecute_SuccessWithServings() {
        LocalDate date = LocalDate.of(2023, 11, 11);
        when(scannerMock.nextLine())
                .thenReturn("2023/11/11")
                .thenReturn("Breakfast")
                .thenReturn("1")
                .thenReturn("2")
                .thenReturn("");
        try (var mockStatic = Mockito.mockStatic(DateParser.class)) {
            mockStatic.when(() -> DateParser.parse("2023/11/11")).thenReturn(date);

            Command result = logFoodCommand.execute();

            assertEquals(logFoodCommand, result);
        }
    }

    @Test
    void testExecute_SuccessWithCustomSize() {
        LocalDate date = LocalDate.of(2023, 11, 11);
        when(scannerMock.nextLine())
                .thenReturn("2023/11/11")
                .thenReturn("Lunch")
                .thenReturn("1")
                .thenReturn("")
                .thenReturn("150");
        try (var mockStatic = Mockito.mockStatic(DateParser.class)) {
            mockStatic.when(() -> DateParser.parse("2023/11/11")).thenReturn(date);

            Command result = logFoodCommand.execute();

            assertEquals(logFoodCommand, result);
        }
    }

    @Test
    void testExecute_InvalidDate() {
        when(scannerMock.nextLine()).thenReturn("invalid-date");
        try (var mockStatic = Mockito.mockStatic(DateParser.class)) {
            mockStatic.when(() -> DateParser.parse("invalid-date"))
                    .thenThrow(new IllegalArgumentException("Invalid date format"));

            Command result = logFoodCommand.execute();

            assertNull(result);
        }
    }

    @Test
    void testToString() {
        assertEquals("log food", logFoodCommand.toString());
    }
}
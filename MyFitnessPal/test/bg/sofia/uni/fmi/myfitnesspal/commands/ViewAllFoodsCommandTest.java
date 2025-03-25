package bg.sofia.uni.fmi.myfitnesspal.commands;

import bg.sofia.uni.fmi.myfitnesspal.Controller;
import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ViewAllFoodsCommandTest {
    private Controller controllerMock;
    private ViewAllFoodsCommand viewAllFoodsCommand;

    @BeforeEach
    void setUp() {
        controllerMock = mock(Controller.class);
        viewAllFoodsCommand = new ViewAllFoodsCommand(controllerMock);
    }

    @Test
    void testExecute_WithFoods() {
        Map<String, Consumable> items = new HashMap<>();
        Food food = new Food.Builder("Banana").servingSize(120).calories(89).carbs(23.0).fat(0.3).protein(1.1).build();
        items.put("Banana", food);
        when(controllerMock.getItems()).thenReturn(items);

        Command result = viewAllFoodsCommand.execute();

        assertEquals(viewAllFoodsCommand, result);
    }

    @Test
    void testExecute_NoFoods() {
        when(controllerMock.getItems()).thenReturn(new HashMap<>());

        Command result = viewAllFoodsCommand.execute();

        assertEquals(viewAllFoodsCommand, result);
    }

    @Test
    void testToString() {
        assertEquals("view all foods", viewAllFoodsCommand.toString());
    }
}
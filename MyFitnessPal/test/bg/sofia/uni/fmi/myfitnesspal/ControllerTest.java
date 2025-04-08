package bg.sofia.uni.fmi.myfitnesspal;

import bg.sofia.uni.fmi.myfitnesspal.commands.Command;
import bg.sofia.uni.fmi.myfitnesspal.commands.CommandFactory;
import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.items.Meal;
import bg.sofia.uni.fmi.myfitnesspal.items.Water;
import bg.sofia.uni.fmi.myfitnesspal.serializer.ItemSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ControllerTest {

    private Controller controller;

    @Mock
    private CommandFactory mockCommandFactory;

    @Mock
    private ItemSerializer mockSerializer;

    @Mock
    private Command mockCommand;

    @Mock
    private Food mockFood;

    @Mock
    private Meal mockMeal;

    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        when(mockSerializer.readData()).thenReturn(false);

        controller = spy(new Controller(mockSerializer, mockCommandFactory));

        Field serializerField = Controller.class.getDeclaredField("serializer");
        serializerField.setAccessible(true);
        serializerField.set(controller, mockSerializer);

        Field commandFactoryField = Controller.class.getDeclaredField("commandFactory");
        commandFactoryField.setAccessible(true);
        commandFactoryField.set(controller, mockCommandFactory);
    }

    @Test
    void testDefaultConstructorInitializesCorrectly() throws NoSuchFieldException, IllegalAccessException {
        outContent.reset();

        when(mockSerializer.readData()).thenReturn(false);
        Controller testController = spy(new Controller(mockSerializer, mockCommandFactory));

        Field serializerField = Controller.class.getDeclaredField("serializer");
        serializerField.setAccessible(true);
        serializerField.set(testController, mockSerializer);

        assertTrue(testController.getItems().containsKey("water"));
        assertNotNull(testController.getFoodIds());
        assertNotNull(testController.getMealIds());
    }

    @Test
    void testConstructorInitializesCorrectly() {
        Map<String, Consumable> items = controller.getItems();
        assertNotNull(items);
        assertTrue(items.containsKey("water"));
        assertTrue(items.get("water") instanceof Water);
    }

    @Test
    void testExecuteCommandExecutesSuccessfully() {
        when(mockCommandFactory.getCommand("test")).thenReturn(mockCommand);
        when(mockCommand.isExitCommand()).thenReturn(false);

        Command result = controller.executeCommand("test");

        verify(mockCommandFactory).getCommand("test");
        verify(mockCommand).execute();
        verify(mockSerializer, never()).saveData();
        assertEquals(mockCommand, result);
    }

    @Test
    void testExecuteCommandSavesDataOnExit() {
        when(mockCommandFactory.getCommand("exit")).thenReturn(mockCommand);
        when(mockCommand.isExitCommand()).thenReturn(true);

        Command result = controller.executeCommand("exit");

        verify(mockCommandFactory).getCommand("exit");
        verify(mockCommand).execute();
        verify(mockSerializer).saveData();
        assertEquals(mockCommand, result);
    }

    @Test
    void testAddFoodUpdatesItemsAndFoodIds() {
        when(mockFood.toString()).thenReturn("apple (100g; 52 kcal; 0.0g, 0.0g, 0.00g)");

        controller.addFood(mockFood);

        assertTrue(controller.getItems().containsKey("apple (100g; 52 kcal; 0.0g, 0.0g, 0.00g)"));
        assertEquals(mockFood, controller.getItems().get("apple (100g; 52 kcal; 0.0g, 0.0g, 0.00g)"));
        assertEquals("apple (100g; 52 kcal; 0.0g, 0.0g, 0.00g)", controller.getFoodIds().get(1));
        assertEquals(1, controller.getCurrentFoodId());
    }

    @Test
    void testAddMealUpdatesItemsAndMealIds() {
        when(mockMeal.getName()).thenReturn("Breakfast");

        controller.addMeal(mockMeal);

        assertTrue(controller.getItems().containsKey("Breakfast"));
        assertEquals(mockMeal, controller.getItems().get("Breakfast"));
        assertEquals("Breakfast", controller.getMealIds().get(1));
        assertEquals(1, controller.getCurrentMealId());
    }

    @Test
    void testGetItemsReturnsCorrectMap() {
        Map<String, Consumable> items = controller.getItems();
        assertNotNull(items);
        assertTrue(items.containsKey("water"));
        assertEquals(1, items.size());
    }

    @Test
    void testGetFoodIdsReturnsCorrectMap() {
        Map<Integer, String> foodIds = controller.getFoodIds();
        assertNotNull(foodIds);
        assertTrue(foodIds.isEmpty());
    }

    @Test
    void testGetMealIdsReturnsCorrectMap() {
        Map<Integer, String> mealIds = controller.getMealIds();
        assertNotNull(mealIds);
        assertTrue(mealIds.isEmpty());
    }

    @Test
    void testGetCurrentFoodIdInitialValue() {
        assertEquals(0, controller.getCurrentFoodId());
    }

    @Test
    void testGetCurrentMealIdInitialValue() {
        assertEquals(0, controller.getCurrentMealId());
    }

    @Test
    void testUpdateCurrentFoodIdNoArgIncrementsCorrectly() {
        int newId = controller.updateCurrentFoodId();
        assertEquals(1, newId);
        assertEquals(1, controller.getCurrentFoodId());

        newId = controller.updateCurrentFoodId();
        assertEquals(2, newId);
        assertEquals(2, controller.getCurrentFoodId());
    }

    @Test
    void testUpdateCurrentFoodIdWithArgSetsCorrectly() {
        int newId = controller.updateCurrentFoodId(5);
        assertEquals(5, newId);
        assertEquals(5, controller.getCurrentFoodId());
    }

    @Test
    void testSetCurrentFoodIdSetsCorrectly() {
        int newId = controller.setCurrentFoodId(10);
        assertEquals(10, newId);
        assertEquals(10, controller.getCurrentFoodId());
    }

    @Test
    void testSetCurrentMealIdSetsCorrectly() {
        int newId = controller.setCurrentMealId(3);
        assertEquals(3, newId);
        assertEquals(3, controller.getCurrentMealId());
    }

    @Test
    void testStartHandlesCommandsAndExceptions() {
        String input = "test\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        when(mockCommandFactory.getCommand("test")).thenThrow(new IllegalArgumentException("Invalid"));
        when(mockCommandFactory.getCommand("exit")).thenReturn(mockCommand);
        when(mockCommand.isExitCommand()).thenReturn(true);

        Thread thread = new Thread(() -> controller.start());
        thread.start();
        try {
            Thread.sleep(100);
            thread.interrupt();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertTrue(outContent.toString().contains("> "));
        assertTrue(outContent.toString().contains("wrong command try another"));
        verify(mockSerializer).saveData();
    }
}
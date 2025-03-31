package bg.sofia.uni.fmi.myfitnesspal;

import bg.sofia.uni.fmi.myfitnesspal.commands.Command;
import bg.sofia.uni.fmi.myfitnesspal.commands.CommandFactory;
import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.items.Water;
import bg.sofia.uni.fmi.myfitnesspal.serializer.ItemSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
    private Scanner mockScanner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new Controller(mockSerializer, mockCommandFactory);
    }

    @Test
    void testDefaultConstructorInitializesCorrectly() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        when(mockSerializer.readData()).thenReturn(false);
        Controller defaultController = new Controller();

        assertTrue(defaultController.getItems().containsKey("water"));
        assertNotNull(defaultController.getFoodIds());
        assertEquals(0, defaultController.getCurrentFoodId());
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
    void testAddFoodAddsToItems() {
        when(mockFood.toString()).thenReturn("apple");

        controller.addFood(mockFood);

        Map<String, Consumable> items = controller.getItems();
        assertTrue(items.containsKey("apple"));
        assertEquals(mockFood, items.get("apple"));
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
    void testGetCurrentFoodIdInitialValue() {
        assertEquals(0, controller.getCurrentFoodId());
    }

    @Test
    void testUpdateCurrentFoodIdIncrementsCorrectly() {
        int newId = controller.updateCurrentFoodId();
        assertEquals(1, newId);
        assertEquals(1, controller.getCurrentFoodId());

        newId = controller.updateCurrentFoodId();
        assertEquals(2, newId);
        assertEquals(2, controller.getCurrentFoodId());
    }

    @Test
    void testUpdateCurrentFoodIdWithParameterSetsCorrectly() {
        int newId = controller.updateCurrentFoodId(5);
        assertEquals(5, newId);
        assertEquals(5, controller.getCurrentFoodId());

        newId = controller.updateCurrentFoodId(10);
        assertEquals(10, newId);
        assertEquals(10, controller.getCurrentFoodId());
    }

    @Test
    void testInitControllerWithNoExistingData() {
        when(mockSerializer.readData()).thenReturn(false);
        controller = new Controller(mockSerializer, mockCommandFactory);
        assertTrue(controller.getItems().containsKey("water"));
    }

    @Test
    void testInitControllerWithExistingData() {
        when(mockSerializer.readData()).thenReturn(true);
        controller = new Controller(mockSerializer, mockCommandFactory);
        assertTrue(controller.getItems().containsKey("water"));
    }
}
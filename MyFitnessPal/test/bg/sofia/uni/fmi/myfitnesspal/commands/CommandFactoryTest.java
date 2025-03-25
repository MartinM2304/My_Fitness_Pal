package bg.sofia.uni.fmi.myfitnesspal.commands;

import bg.sofia.uni.fmi.myfitnesspal.Controller;
import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.items.Water;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CommandFactoryTest {
    private Map<String, Consumable> items;
    private Scanner scannerMock;
    private Controller controllerMock;
    private CommandFactory commandFactory;

    @BeforeEach
    void setUp() {
        items = new HashMap<>();
        scannerMock = mock(Scanner.class);
        controllerMock = mock(Controller.class);
        items.put("water", mock(Water.class));
        items.put("food", mock(Food.class));
        commandFactory = new CommandFactory(items, scannerMock, controllerMock);
    }

    @Test
    void testGetCommand_ValidCommand() {
        Command command = commandFactory.getCommand("CREATE FOOD");
        assertNotNull(command);
        assertEquals("create food", command.toString());
    }

    @Test
    void testGetCommand_InvalidCommand() {
        assertThrows(IllegalArgumentException.class, () -> {
            commandFactory.getCommand("invalid command");
        }, "this is not a valid command");
    }

    @Test
    void testGetCommand_TrimmedAndNormalized() {
        Command command = commandFactory.getCommand("  CHECK WATER  ");
        assertNotNull(command);
        assertEquals("check water", command.toString());
    }

    @Test
    void testCommandFactory_Initialization() {
        Command command = commandFactory.getCommand("log food");
        assertNotNull(command);
        assertEquals("log food", command.toString());

        command = commandFactory.getCommand("view all foods");
        assertNotNull(command);
        assertEquals("view all foods", command.toString());
    }
}
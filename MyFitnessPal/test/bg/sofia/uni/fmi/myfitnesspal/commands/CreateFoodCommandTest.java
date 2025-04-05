package bg.sofia.uni.fmi.myfitnesspal.commands;

import bg.sofia.uni.fmi.myfitnesspal.Controller;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CreateFoodCommandTest {
    private Scanner scannerMock;
    private Controller controllerMock;
    private CreateFoodCommand createFoodCommand;

    @BeforeEach
    void setUp() {
        scannerMock = mock(Scanner.class);
        controllerMock = mock(Controller.class);
        createFoodCommand = new CreateFoodCommand(scannerMock, controllerMock);
        when(controllerMock.getFoodIds()).thenReturn(new HashMap<>());
        when(controllerMock.getCurrentFoodId()).thenReturn(0);
    }

    @Test
    void testExecute_SuccessfulFoodCreation() {
        when(scannerMock.nextLine())
                .thenReturn("Apple")
                .thenReturn("Red fruit")
                .thenReturn("100")
                .thenReturn("1")
                .thenReturn("52")
                .thenReturn("14.0")
                .thenReturn("0.2")
                .thenReturn("0.3");

        Command result = createFoodCommand.execute();

        assertNotNull(result);
        assertEquals(createFoodCommand, result);
        verify(controllerMock).addFood(any(Food.class));
        verify(controllerMock).getFoodIds();
        verify(controllerMock).updateCurrentFoodId();
    }

    @Test
    void testExecute_NoDescription() {
        when(scannerMock.nextLine())
                .thenReturn("Banana")
                .thenReturn("")
                .thenReturn("120")
                .thenReturn("1")
                .thenReturn("89")
                .thenReturn("23.0")
                .thenReturn("0.3")
                .thenReturn("1.1");

        Command result = createFoodCommand.execute();

        assertNotNull(result);
        assertEquals(createFoodCommand, result);
        verify(controllerMock).addFood(any(Food.class));
    }

    @Test
    void testExecute_InvalidNumberInput() {
        when(scannerMock.nextLine())
                .thenReturn("Orange")
                .thenReturn("Citrus")
                .thenReturn("invalid");

        assertThrows(NumberFormatException.class, () -> createFoodCommand.execute());
        verify(controllerMock, never()).addFood(any(Food.class));
    }

    @Test
    void testToString() {
        assertEquals("create food", createFoodCommand.toString());
    }
}
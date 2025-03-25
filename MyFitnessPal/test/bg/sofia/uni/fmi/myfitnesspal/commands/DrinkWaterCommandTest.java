package bg.sofia.uni.fmi.myfitnesspal.commands;

import bg.sofia.uni.fmi.myfitnesspal.date.DateParser;
import bg.sofia.uni.fmi.myfitnesspal.items.Water;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DrinkWaterCommandTest {
    private Water waterMock;
    private Scanner scannerMock;
    private DrinkWaterCommand drinkWaterCommand;

    @BeforeEach
    void setUp() {
        waterMock = mock(Water.class);
        scannerMock = mock(Scanner.class);
        drinkWaterCommand = new DrinkWaterCommand(waterMock, scannerMock);
    }

    @Test
    void testExecute_Success() {
        LocalDate date = LocalDate.of(2023, 11, 11);
        when(scannerMock.nextLine()).thenReturn("2023/11/11").thenReturn("500");
        try (var mockStatic = Mockito.mockStatic(DateParser.class)) {
            mockStatic.when(() -> DateParser.parse("2023/11/11")).thenReturn(date);

            Command result = drinkWaterCommand.execute();

            assertEquals(drinkWaterCommand, result);
            verify(waterMock).consumpt(date, 500);
        }
    }

    @Test
    void testExecute_InvalidQuantity() {
        when(scannerMock.nextLine()).thenReturn("2023/11/11").thenReturn("invalid");
        try (var mockStatic = Mockito.mockStatic(DateParser.class)) {
            mockStatic.when(() -> DateParser.parse("2023/11/11")).thenReturn(LocalDate.of(2023, 11, 11));

            assertThrows(NumberFormatException.class, () -> drinkWaterCommand.execute());
            verify(waterMock, never()).consumpt(any(), anyInt());
        }
    }

    @Test
    void testToString() {
        assertEquals("drink water", drinkWaterCommand.toString());
    }
}
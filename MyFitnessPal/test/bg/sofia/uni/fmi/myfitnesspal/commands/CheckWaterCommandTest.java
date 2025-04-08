package bg.sofia.uni.fmi.myfitnesspal.commands;

import bg.sofia.uni.fmi.myfitnesspal.date.DateParser;
import bg.sofia.uni.fmi.myfitnesspal.items.Water;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.ConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.WaterConsumptionEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckWaterCommandTest {
    private Water waterMock;
    private Scanner scannerMock;
    private CheckWaterCommand checkWaterCommand;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        waterMock = mock(Water.class);
        scannerMock = mock(Scanner.class);
        checkWaterCommand = new CheckWaterCommand(waterMock, scannerMock);
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void testExecute_ValidDateWithConsumption() {
        String inputDate = "11.11.1111";
        LocalDate parsedDate = LocalDate.of(1111, 11, 11);
        WaterConsumptionEntry entry1 = new WaterConsumptionEntry(500);
        WaterConsumptionEntry entry2 = new WaterConsumptionEntry(300);
        List<ConsumptionEntry> mockConsumption = Arrays.asList(entry1, entry2);

        when(scannerMock.nextLine()).thenReturn(inputDate);
        try (var mockStatic = Mockito.mockStatic(DateParser.class)) {
            mockStatic.when(() -> DateParser.parse(inputDate)).thenReturn(parsedDate);
            when(waterMock.getConsumptionForDate(parsedDate)).thenReturn(mockConsumption);

            Command result = checkWaterCommand.execute();

            assertNotNull(result);
            assertEquals(checkWaterCommand, result);
        }
    }

    @Test
    void testExecute_ValidDateNoConsumption() {
        String inputDate = "11.11.2023";
        LocalDate parsedDate = LocalDate.of(2023, 11, 11);

        when(scannerMock.nextLine()).thenReturn(inputDate);
        try (var mockStatic = Mockito.mockStatic(DateParser.class)) {
            mockStatic.when(() -> DateParser.parse(inputDate)).thenReturn(parsedDate);
            when(waterMock.getConsumptionForDate(parsedDate))
                    .thenThrow(new IllegalArgumentException("you didnt consume any items then"));

            Command result = checkWaterCommand.execute();

            assertNull(result);
        }
    }

    @Test
    void testExecute_InvalidDateFormat() {
        String invalidDate = "invalid-date";
        when(scannerMock.nextLine()).thenReturn(invalidDate);

        try (var mockStatic = Mockito.mockStatic(DateParser.class)) {
            mockStatic.when(() -> DateParser.parse(invalidDate))
                    .thenThrow(new IllegalArgumentException("Invalid date format"));

            Command result = checkWaterCommand.execute();

            assertNull(result);
            assertTrue(outContent.toString().contains("Invalid date format"));
        }
    }

    @Test
    void testIsExitCommand() {
        assertFalse(checkWaterCommand.isExitCommand());
    }
}
package bg.sofia.uni.fmi.myfitnesspal.commands;

import bg.sofia.uni.fmi.myfitnesspal.date.DateParser;
import bg.sofia.uni.fmi.myfitnesspal.items.Water;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CheckWaterCommandTest {
    private Water waterMock;
    private Scanner scannerMock;
    private CheckWaterCommand checkWaterCommand;

    @BeforeEach
    void setUp() {
        waterMock = mock(Water.class);
        scannerMock = mock(Scanner.class);
        checkWaterCommand = new CheckWaterCommand(waterMock, scannerMock);
    }

    @Test
    void testExecute_ValidDateWithConsumption() {
        String inputDate = "1111.11.11";
        LocalDate parsedDate = LocalDate.of(1111, 11, 11);
        List<Integer> mockConsumption = Arrays.asList(500, 300);

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
        String inputDate = "2023.11.11";
        LocalDate parsedDate = LocalDate.of(2023, 11, 11);

        when(scannerMock.nextLine()).thenReturn(inputDate);
        try (var mockStatic = Mockito.mockStatic(DateParser.class)) {
            mockStatic.when(() -> DateParser.parse(inputDate)).thenReturn(parsedDate);
            when(waterMock.getConsumptionForDate(parsedDate)).thenThrow(new IllegalArgumentException("you didnt drink any water then"));

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

            assertThrows(IllegalArgumentException.class, checkWaterCommand::execute);
        }

    }
}